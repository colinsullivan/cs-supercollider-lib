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

    var me = this,
      onDone = {};

    // if a callback was passed in, use that as the done loading callback
    if (aCallback != nil, {
      onDone = aCallback;
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

          this.bufs[bufKey] = buf;

          ("loaded buf: " ++ bufKey).postln();

          // if all bufs are not zero
          if (bufList.any({ arg bufData; this.bufs[bufData[1].asSymbol()] == 0; }) == false, {
            // finish loading
            onDone.value();
          });
        };
      );
    });
  }

  cue_bufs {
    arg bufList;

    bufList.do({
      arg bufData;

      var bufFileName = bufData.bufferFileName,
        bufKey = bufData.bufferKey,
        numChannels = bufData.numChannels;
      
      this.bufs[bufKey] = Buffer.cueSoundFile(
        Server.default,
        this.rootDir +/+ bufFileName,
        numChannels: numChannels
      );
      ("cued buf: " ++ bufKey).postln();
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
        env,
        trackSeqs;

      //("Loading midifile " ++ midiFileName).postln();

      if (tempoBPM != nil, {
        tempo = params['tempoBPM'] / 60.0;
        midifile.tempo = tempo;
        if (makeDuration != nil, {
          makeDurationSecs = makeDuration / tempo;
        });
      });
      midifile.read();
/**

Pulled from `extSimpleMIDIFile-patterns.sc`.

Returns an array of [note, dur] sequence info for each track, with proper accounting of rests. Ê

Thus, if m is a SimpleMIDIFile, you could do the following:

t = m.generatePatternSeqs; // t is now an arry of [note, dur] info for each track

Pbind( [\midinote, \dur], Pseq(t[1], 1)).play;

Note that the first track in a SimpleMIDIFile often contains no note events if imported from an external midi file (since it's used for metadata), so that the first track of interest is usually the one in index 1 of the getSeqs array.Ê I decided to leave the first blank track in so preserve the mapping from midi track # to getSeqs array #.

**/
		
      midifile.timeMode_('ticks');
      trackSeqs = Array.fill(midifile.tracks, {List.new(0)});
      
      midifile.noteEvents.do({|event| // sort the tracks in to separate Lists, which are stored in ~tracks
        var trackNum = event[0];
        trackSeqs[trackNum].add(event);
      });
      
      trackSeqs = trackSeqs.collect({|track|
        var trackEvents, seq;
        seq = List.new(0);

        trackEvents = track.clump(2).collect({|pair|
          (
            'dur': pair[1][1] - pair[0][1],
            'note': pair[0][4],
            'startPos': pair[0][1],
            'endPos': pair[1][1]
          )
        });

        trackEvents.do({|event, i|
          var diff;
          if (i==0,Ê
            {	
              if (event.startPos != 0, {
                seq.add([\rest, event.startPos]);
              });
              seq.add([event.note, event.dur]);
            },
            {
              diff = event.startPos - trackEvents[i-1].endPos;
              if (diff > 0,
                {
                  seq.add([\rest, diff]);
                  seq.add([event.note, event.dur]);
                },
                {
                  seq.add([event.note, event.dur]);
                }
              )
            }
          );
        });
        seq.collect({|pair| [pair[0], pair[1] / midifile.division]});
      });
      
      // assuming single-track MIDI files for now
      this.midiSequences[midiKey] = trackSeqs[0];
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
          minValue = 127,
          numNodes = 0;

        if (midifile.controllerEvents(cc).size() > 0, {
          midiCCEnvs[midiKey][cc] = midifile.envFromCC(
            track: 0,
            cc: cc
          );

          midiCCEnvs[midiKey][cc].levels.do({
            arg level;
            numNodes = numNodes + 1;
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

          midiCCEnvs[midiKey][cc].releaseNode = numNodes - 1;

          if (makeDuration != nil, {
            midiCCEnvs[midiKey][cc].duration = makeDurationSecs;
          });
        });
      });
    });

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
