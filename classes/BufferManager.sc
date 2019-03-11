/**
 *  @file       BufferManager.sc
 *
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

/**
 *  @class        BufferManager
 *
 *  @classdesc    Loads assets and stores them keyed by an identifier.
 *                Currently supporting two types of assets: audio files (
 *                loaded as buffers) and MIDI files (loaded as event
 *                sequences).
 **/
BufferManager : Object {
  
  var <>bufs,
    <>midiSequences,
    <>midiCCEnvs,
    doneLoadingCallback,
    <>rootDir;

  *new {
    arg params;

    ^super.new.init(params);
  }

  /**
   *  Initialize
   *
   *  @param  {String}    params.rootDir - The root directory we will look
   *          for assets in.
   *  @param  {Function}  params.doneLoadingCallback - Callback to fire when
   *          assets are done loading & preprocessing.
   */
  init {
    arg params;
    this.rootDir = params[\rootDir];
    doneLoadingCallback = {};
    this.bufs = ();
    this.midiSequences = ();
    this.midiCCEnvs = ();
  }

  /**
   *  Call to load audio buffers.
   *
   *  @param  {Array}  bufList - An array of arrays, each sub-array contains:
   *                   [filenameString, keySymbol]
   *                   filenameString: string of filename in `rootDir`
   *                   keySymbol: a symbol to use as a lookup key for buffer
   *  @param  {Function}  aCallback - A callback to use when completed.
   */
  load_bufs {
    arg bufList,
      aCallback;

    var me = this;

    // if a callback was passed in, use that as the done loading callback
    if (aCallback != nil, {
      doneLoadingCallback = aCallback;    
    });

    // first we need to know about all the buffers we are trying to load
    bufList.do({
      arg bufData;

      var bufFileName = bufData[0],
        bufKey = bufData[1].asSymbol();

      this.bufs[bufKey] = 0;

    });

    // now actually load them all
    bufList.do({
      arg bufData;

      var bufFileName = bufData[0],
        bufKey = bufData[1].asSymbol();
      
      Buffer.read(
        Server.default,
        this.rootDir +/+ bufFileName,
        action: {
          arg buf;

          me.buf_loaded(bufKey, buf);
        };
      );
    });
  }

  /**
   *  Load a list of MIDI files.  Assumed they are single track MIDI files.
   *  They are parsed with the SimpleMIDIFile class (found in the wslib quark)
   *  into a simple list of [midiNote, dur] arguments that can be passed
   *  into a Pbind.
   *
   *  @param  {Array}  midiParams - Array of objects, each with the following:
   *                   filenameString: filename in `rootDir`
   *                   keySymbol: a symbol to use as a lookup key for midi
   *                   makeDuration: An integer number of beats for the length,
   *                   if specified will add a rest at the end to make total
   *                   duration equal this.
   *                   ccsToEnv: A list of CC numbers which will be converted
   *                    to envs.
   **/
  load_midi {
    arg midiParams;

    midiParams.do({
      arg params;

      var midiFileName = params['midiFileName'],
        midiKey = params['midiKey'].asSymbol(),
        makeDuration = params['makeDuration'],
        ccsToEnv = params['ccsToEnv'],
        tempoBPM = params['tempoBPM'],
        tempo,
        midifile = SimpleMIDIFile.new(rootDir +/+ midiFileName),
        durationSum = 0,
        makeDurationSecs,
        env;

      if (tempoBPM != nil, {
        tempo = params['tempoBPM'] / 60.0;
        midifile.tempo = tempo;
        if (makeDuration != nil, {
          makeDurationSecs = makeDuration / tempo;
        });
      });
      midifile.read();

      // assuming single-track MIDI files for now
      this.midiSequences[midiKey] = midifile.generatePatternSeqs()[0];
      this.midiCCEnvs[midiKey] = ();


      // Appends a rest the end of the notes list to make duration match
      // the `makeDuration` argument.
      if (makeDuration != nil, {    
        // sum all durations
        midiSequences[midiKey].do({
          arg midiEvent;
          durationSum = durationSum + midiEvent[1];
        });
        // add missing rest to sequence list
        if (makeDuration > durationSum, {
          midiSequences[midiKey].add([\rest, makeDuration - durationSum]);
        });
      });
      //"this.midiSequences[midiKey]:".postln;
      //this.midiSequences[midiKey].postln;
      //"midifile.noteEvents:".postln;
      //midifile.noteEvents.postln;

      ccsToEnv.do({
        arg cc;
        var maxValue = 0,
          minValue = 127;

        if (midifile.controllerEvents(cc).size() > 0, {
          midiCCEnvs[midiKey][cc] = midifile.envFromCC(
            track: 0,
            cc: cc
          );

          midiCCEnvs[midiKey][cc].levels.do({
            arg level;
            if (level > maxValue, {
              maxValue = level;
            });
            if (level < minValue, {
              minValue = level;
            });
          });

          midiCCEnvs[midiKey][cc] = midiCCEnvs[midiKey][cc].range(
            minValue / 127.0,
            maxValue / 127.0
          );

          if (makeDuration != nil, {
            midiCCEnvs[midiKey][cc].duration = makeDurationSecs;
          });
        });
      });
    });

  }

  buf_loaded {
    arg bufKey, buf, msg;

    this.bufs[bufKey] = buf;

    ("loaded buf: " ++ bufKey).postln();

    // if all bufs are not zero
    if (this.bufs.any({ arg item; item == 0; }) == false, {
      // finish loading
      this.bufs_all_loaded();
    }, {
      msg = "bufs to load:";

      this.bufs.keysValuesDo({
        arg bufKey, bufValue;

        if (bufValue == 0, {
          msg = msg ++ bufKey ++ ", ";
        });
      });

      msg.postln();
    });
  }

  bufs_all_loaded {

    doneLoadingCallback.value();

  }
  get_buffer_section {
    arg bufName,
      startSeconds,
      endSeconds,
      channels;

    var startFrame, endFrame, buf;

    buf = bufs[bufName];

    startFrame = buf.numFrames * (startSeconds / buf.duration);
    endFrame = buf.numFrames * (endSeconds / buf.duration);

    ^Buffer.readChannel(
      Server.default,
      buf.path,
      startFrame: startFrame,
      numFrames: endFrame - startFrame,
      channels: channels
    );
  }
}
