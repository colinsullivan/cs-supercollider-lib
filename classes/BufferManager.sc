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
   *  @param  {Array}  midiList - Array of arrays, each with the following:
   *                   [filenameString, keySymbol, treatAsLoopBoolean]
   *                   filenameString: filename in `rootDir`
   *                   keySymbol: a symbol to use as a lookup key for midi
   *                   makeDuration: An integer number of beats for the length,
   *                   if specified will add a rest at the end to make total
   *                   duration equal this.
   **/
  load_midi {
    arg midiList;

    midiList.do({
      arg midiData;

      var midiFileName = midiData[0],
        midiKey = midiData[1].asSymbol(),
        makeDuration = midiData[2],
        midifile = SimpleMIDIFile.read(rootDir +/+ midiFileName),
        durationSum = 0;

      // assuming single-track MIDI files for now
      this.midiSequences[midiKey] = midifile.generatePatternSeqs()[0];

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
}
