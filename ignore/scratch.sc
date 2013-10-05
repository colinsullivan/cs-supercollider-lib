({
  s.quit();
  /*s.options.inDevice = "PreSonus FIREPOD (2112)";*/
  s.options.outDevice= "JackRouter";
  /*s.options.sampleRate = 48000;*/
  s.boot();
  s.meter();
  //s.scope();
  //FreqScope.new(400, 200);

  s.doWhenBooted({
    Instr.dir = "/Users/colin/Projects/cs-supercollider-lib/Instr/";
    Instr.loadAll();
  });
}.value());

({FBSineC.ar(800, 1.0, 1.0);}.play();)

// can we make some soft tones
(
  Instr(\softtest, {
    arg freq = 440, gate = 0, amp;

    var out,
      outEnvShape;

    out = SinOsc.ar(freq);

    outEnvShape = Env.adsr(1.0, 0.0, 1.0, 2.0, 1.0, [2, -2, -5]);
    outEnvShape.plot();
    out = amp * EnvGen.kr(outEnvShape, gate, doneAction: 2) * out;

    out;

  }, [
    \freq,
    \gate,
    \amp
  ]).miditest([3, 0]);
)


// awesome synthetic harsh ambience
(
{
  var out,
    trig,
    modIndex,
    modIndexLow = 40,
    modIndexHigh = 80,
    feedbackModulator;

  feedbackModulator = MouseY.kr(0.0, 1.0);

  trig = GaussTrig.ar(SinOscFB.kr(0.05, feedbackModulator).range(8, 12));

  //modIndex = SinOscFB.kr(SinOscFB.kr(0.05, 1.0).range(0.5, 0.8), 1.0).range(0.1, 50);
  modIndex = SinOscFB.kr(FBSineC.ar(20, 1.0, feedbackModulator), feedbackModulator).exprange(modIndexLow, modIndexHigh);

  out = GrainFM.ar(
    2,
    //trigger: Dust2.kr(10),
    //trigger: Impulse.kr(10),
    trigger: trig,
    dur: SinOscFB.kr(0.05, feedbackModulator).range(0.1, 1.0),
    carfreq: 440,
    modfreq: 200,
    index: modIndex,
    pan: 0,
    envbufnum: -1,
    maxGrains: 512
  );

}.play();
)

({Help.gui;}.value());

Quarks.gui;

(s.meter;)


({
  /*x = Synth.new("colinsul-fmSqueal", ["carrierBaseFreq", 220, "filterTopFreq", 20000]);*/

  var onTime = 1.0,
    offTime = 0.5,
    totalTime = onTime + offTime,
    onTimePerc = onTime / totalTime,
    offTimePerc = offTime / totalTime;

  /*PmonoArtic("colinsul-fmSqueal",*/
    /*\filterTopFreq,       Pseq([  4000,   8000,   ], 1),*/
    /*\carrierBaseFreq,     Pseq([  440,    880,    ], 1),*/
    /*\ampEnvAttackTime,    Pseq([  1.0,    0.5,    ], 1),*/
    /*\ampEnvReleaseTime,   Pseq([  0.5,    2.0,    ], 1),*/
    /*\legato,              Pseq([  0.9,    0.9     ], 1),*/
    /*\dur,                 Pseq([  3.0,    4.0,    ], 1)*/
  /*).play;*/

  /*Pmono("colinsul-fmSqueal",*/
    /*\carrierBaseFreq,   Pseq([  220,    220,    440,    440,    880,    880,    1760,   1760,     ], 1),*/
    /*\filterTopFreq,     Pseq([  2000,   2000,   4000,   4000,   8000,   8000,   16000,  16000,    ], 1),*/
    /*[>\gate,              Pseq([  1,      0,      1,      0,      1,      0,      1,      0,        ], 1),<]*/
    /*[>\legato,            Pseq([  onTimePerc,    offTimePerc,    onTimePerc,    offTimePerc,    onTimePerc,    offTimePerc,    onTimePerc,    offTimePerc,      ], 1),<]*/
    /*\dur,               Pseq([  4.0,      Rest(0.5),      1,      Rest(0.5),      1,      Rest(0.5),      1,      Rest(0.5),        ], 1),*/
  /*).play;*/

}.value());

(
    {
        
    /*var pitchedSynth = {*/
      /*// articulate filter and osc frequency simultaneously*/
    
      /*{*/
          
          var carrierBaseFreq=MouseY.kr(440, 220, 1), filterModulatorTop=MouseY.kr(500, 20000, 1);
          var carrier, modulator, filterModulator;
        
          modulator = SinOsc.kr(freq: 5.0);
          carrier = LFSaw.ar(freq: carrierBaseFreq + modulator*100);
          filterModulator = SinOsc.kr(freq: 0.05, phase: 3*pi/2.0)
            .range(150, filterModulatorTop);
        
          Pan2.ar(
              MoogFF.ar(in: carrier, freq: filterModulator),
              0.5
          );
      /*};*/
    
    /*};*/
    
    /*var theSynth = pitchedSynth.value(, );*/
    /*pitchedSynth.value(carrierBaseFreq:)*/
    /*var carrierBaseFreq = EnvGen.kr(*/
        /*Env.new(),*/
        /*1.0*/
    /*);*/
    
    
    }.play()
);
(
// Play it with the mouse...
/*x = { MoogFF.ar(WhiteNoise.ar(01.1), MouseY.kr(100, 10000, 1), MouseX.kr(0, 4)) }.play(s);*/

x = {
    MoogFF.ar(
        Pulse.ar([40,121], [0.3,0.7]),
        SinOsc.kr(LFNoise0.kr(0.42).range(0.001, 2.2)).range(30, 4200),
        0.83 * 4)}.play(s);
);

(
  {
    /*Pan2.ar(PinkNoise.ar(0.2), SinOsc.kr(0.5).range(-0.75, 0.75))

    PinkNoise.ar(0.2) + SinOsc.ar(440, 0, 0.2)

     Mix array of channels down to a single channel or an array of arrays
        of channels down to a single array of channels */
    Mix.new([SinOsc.ar(440, 0, 0.2), Saw.ar(660, 0.2)]).postln
    /*var a, b;
    a = [SinOsc.ar(440, 0, 0.2), Saw.ar(662, 0.2)];
    b = [SinOsc.ar(442, 0, 0.2), Saw.ar(660, 0.2)];
    Mix([a, b]).postln;
    
     Mix.fill examples 
    var n = 20;
    Mix.fill(n, { arg i;
      SinOsc.ar(i*200, 0, 1/n)
    })
*/
  }.play

  /*{
    PinkNoise.ar(0.2) + SinOsc.ar(440, 0, 0.2) + Saw.ar(660, 0.2)
  }.plot;*/
);

/* Patterns */
(
SynthDef(\smooth, { |freq = 440, sustain = 1, amp = 0.5|
	var	sig;
	sig = SinOsc.ar(freq, 0, amp) * EnvGen.kr(Env.linen(0.05, sustain, 0.1), doneAction: 2);
	Out.ar(0, sig ! 2)
}).add;
);

(
p = Pbind(
		// the name of the SynthDef to use for each note
	\instrument, \smooth,
		// MIDI note numbers -- converted automatically to Hz
	\midinote, Pseq([ 60,   72, 71, 67, 69, 71, 72, 60, 69, 67], 1),
		// rhythmic values
	\dur, Pseq([      2,    2,  1, 0.5, 0.5, 1, 1, 2, 2, 3], 1)
).play;
);

(
// the SynthDef
SynthDef(\test, { | out, freq = 440, amp = 0.1, nharms = 10, pan = 0, gate = 1 |
        var audio = Blip.ar(freq, nharms, amp);
        var env = Linen.kr(gate, doneAction: 2);
        OffsetOut.ar(out, Pan2.ar(audio, pan, env) );
}).load;

// Events are written as parantheses enclosing key/value pairs
(instrument: \test).play;
/*(instrument: \test, freq: 20, nharms: 50).play;*/
);

/**
 *  Timing examples
 **/
({
  var timeNow = TempoClock.default.beats;
  TempoClock.default.tempo = 2; // 120 BPM
  "Time is now: ".post; timeNow.postln;
  "Scheduling for: ".post; (timeNow + 5).postln;
  TempoClock.default.schedAbs(timeNow + 5, {
    "Time is later: ".post; thisThread.clock.beats.postln; nil
  });
}.value(););

({
  var r = Routine({
    "abcde".yield;
    "fghij".yield;
    "klmno".yield;
    "pqrst".yield;
    "uvwxy".yield;
    "z{|}~".yield;
  });
  6.do({ r.next.postln; });
}.value(););

/**
 *  Percussive sounds
 **/
({
  "/Users/colin/Projects/sounds/lib/subtleTwang/examples.sc".load;
}.value());


({

  Quarks.gui;

}.value());

(
  var bass, bassPat;

  bass = Instr("cs.synths.DubBass").asSynthDef().play;
  bassPat = Pbind(
    \type,            \set,
    \id,              bass.nodeID,
    \note,            Pseq([  0,    3,    7,    7,    0,    9,      3,    0     ], 1),
    \root,            4,
    \octave,          Pseq([  3,    3,    3,    3,    3,    2,      3,    2     ], 1),
    \rateMultiplier,  Pseq([  1/2,  6,    6,    6*2,  2,    8,      6,    6*2   ], 1),
  ).play;
  
  (
    bassPat.stop;
    bass.free;
  )
)

(
  Pmono(Instr("synths.DubBass").add.asDefName,
    \note,            Pseq([  0,    3,    7,    7,    0,    9,      3,    0     ], 1),
    \root,            4,
    \octave,          Pseq([  3,    3,    3,    3,    3,    2,      3,    2     ], 1),
    \rateMultiplier,  Pseq([  1/2,  6,    6,    6*2,  2,    8,      6,    6*2   ], 1),
  ).play;
)



(s.meter;)

(
  var amp = 1.0;

  /*Env.adsr(
    attackTime: 0.05,
    decayTime: 0.1,
    sustainLevel: 0.5 * amp,
    releaseTime: 0.1,
    peakLevel: amp,
    curve: [4, -4, -2]
  ).plot;*/

  /*Env.cutoff(2, 1.0).plot;*/

  /*Env.new(
    //initial attack             peak          exp. decay
    [0.0,   0.75,     0.9,      1.0,      0.9,      0.0 ],
    [   0.001,    0.1,     0.05,      0.05,   0.79      ],
    [0.0,   2.5,      -2.5,     2.5,      -2.5,   -5.5  ]
  ).plot();*/

  Env.new(
    [1.0,    1.0,     0.0],
    [     1.0,    0.1],
    releaseNode: nil
  ).plot();

  /*Env.perc(
    attackTime: 0.01,
    releaseTime: 1.0,
    curve: -6
  ).plot();*/

)

(
  var p;
  
  Instr.dir = "/Users/colin/Projects/sounds/lib/";


  /*Instr("fm.bass").miditestMono();*/

  Instr("cheerful", {
    arg freq = 440,
      amp = 0.9,
      gate = 0;

    var envShape,
      outEnv,
      out,
      core,
      ampmod;


    /*core = Saw.ar(freq);*/
    /*core = BlitB3Square.ar(freq: freq, leak: 0.5);*/
    core = PulseDPW.ar(freq: freq);

    // amplitude modulator
    ampmod = SinOsc.kr(
      freq: freq
    ).range(0.7, 1.0);

    core = core * ampmod;

    envShape = Env.adsr(
      attackTime: 0.05,
      decayTime: 0.2,
      sustainLevel: 0.7 * amp,
      releaseTime: 0.5,
      peakLevel: amp,
      curve: [4, -4, -2]
   );
    envShape.releaseNode = 1;

    outEnv = EnvGen.kr(
      envelope: envShape,
      gate: gate,
      doneAction: 2
    );
    
    out = outEnv * core;

    /*core = LPF.ar(out, freq: 160);*/
    /*out = BLowPass(out, MouseX.kr(10, 20000, \exponential), MouseY.kr(0.0, 1.0));*/
    /*out = MoogFF.ar(out, MouseY.kr(100, 10000, 1).poll, MouseX.kr(0, 4).poll);*/
    out = MoogFF.ar(out, 700, 1.25);

  });

  Instr("cheerful").miditest();
)


// modulate fundamental frequency, formant freq stays constant
{ Formant.ar(XLine.kr(50,2000, 8), 5000, 2000, 0.125) }.play


(
  Instr.at("cheerful").miditest();

)

({
  var startingFreq = ([1760, 880, 440, 220].scramble)[0],
    endingFreq = startingFreq / 2.0,
    attackTime = ([0.1, 0.5].scramble)[0],
    releaseTime = ([0.0]).scramble[0],
    freqEnv = EnvGen.kr(Env.perc(attackTime, releaseTime, 1.0, 1, \welch), doneAction: 0).range(startingFreq, endingFreq),
    outEnv = EnvGen.kr(Env.perc(attackTime, releaseTime, 1.0, 1, \welch), doneAction: 2),
    out;

  out = outEnv * SinOsc.ar(freqEnv);

  Out.ar(4, out);
}.play;)


(	
			c = Conductor.make { | conductor, a, b, c, d |  };    
			c.show;
		)
(	// here the CV d is initialized to an array of values.
  c = Conductor.make { | conductor, a, b, c, d | d.value_(1/(1..128)) };    
  c.show;
)
(	
  // define a conductor using the default controlspecs
  Conductor.specs[\spT] = ControlSpec(-60, 700, 'linear', 0, 33);
  a = Conductor.make{ | con, freq1, db, pan, dur, spT3, s3pT, sp3T|	
    con.name_("example 1");
    con.pattern_(Pbind(*[freq: freq1, db: db, pan: pan, dur: dur]) );
  };
  a.show;
)
(
  a.play;
  a[\freq].value = 700;
)

(
a = Conductor.make{| con, a, rate|
	rate	.sp(10, 0, 10); 
	~touchA = a.touch(rate);
	con.gui.keys = #[a, touchA, rate];	
};

a.show;

) 

(
  Instr.dir = "/Users/colin/Projects/sounds/cs-supercollider-lib/Instr";
  Instr.loadAll();
  InstrBrowser.new.gui();
)

/*(
  Instr("sfx.RunningWaterStream").specs
)*/

(
  // action_can control any kind of user program, 
    c = Conductor.make { |conductor, freq, db, dur | 
      freq	.spec_(\freq);
      db		.spec_(\db, -10);
      dur		.sp(0.2, 0.05, 1, 0, 'exp');
      
      // add a pattern using actions,
      // notice the use of ~player, an environment variable
      // within the Conductor
      conductor.action_(
        { ~pat = Pbind(*[freq: freq * 2, db: db, dur: dur/2])
                .play(quant: 0);
        },
        { ~pat.stop },
        { ~pat.pause},
        { ~pat.resume}
      );
      conductor.name_("test");
    };    
    c.show;
)

(
  var p;

  Instr("fm.percTest", {


  });

  Tempo.bpm = 30;
  p = Patch("fm.Lazers").play();


)

(
  Pbind(
    \type,    \instr,
    \instr,   "cs.fm.Kick",
    \freq,    55,
    \dur,     Pseq([1.0], 1),
    \amp,     1.0,
    \legato,  0.5,
    \trig,    0,
    \gate,    1
  ).play;
)

(
  Pbind(
    \type,    \instr,
    \instr,   "cs.fm.Lazers",
    \dur,     Pseq([(1/16)], 48),
    \amp,     1.0,
    \modIndex,  Pfunc({ exprand(0.01, 6); }),
    \mod2Index,  Pfunc({ exprand(0.02, 6); })
  ).play;
)

(
  /*~testPat = Pbind(*[
    instrument: Instr("fm.Kick").asDefName(),
    dur: 0.5
  ]).play();*/
    /*\type,        \instr,
    \instr,       Instr("fm.Lazers"),
    \modIndex,    Pseq([2.5, 0.5], inf)
  );*/

  ~testPat = Pbind(*[
    type: \instr,
    instr: "fm.Kick",
    freq: 55,
    dur: 1.0,
    legato: 1.0
  ]).play();
)

Tempo.bpm = 60;

(
  ~testPat.stop();
)

(
  FreqScope.new(400, 200, 0);

  {
    var source = WhiteNoise.ar(),
      out;

    out = source;

    /*out = LPF.ar(out,
      freq: 8000
    );*/
    /*out = BBandStop.ar(out, 
      freq: 20000,
      bw: 2
    );*/
    /*out = MidEQ.ar(out, 440, 0.1, 6.0);*/

    out = BPeakEQ.ar(out,
      freq: 23000,
      rq: 5,
      db: -16
    );

    Out.ar(0, out);
  }.play();
)

\gate.asSpec();


(
  MIDIClient.init;
  MIDIClient.sources;
)


(
  var m, mBounds;
  /*s.options.inDevice = "PreSonus FIREPOD (2112)";*/
  s.options.outDevice= "Soundflower (64ch)";
  s.quit;
  s.options.sampleRate = 48000;
  s.options.hardwareBufferSize = 128;
  s.boot();
  s.latency = 0;
  m = s.meter();

  // move level meter to bottom right of screen
  mBounds = m.window.bounds;
  /*mBounds.left = 1680;
  mBounds.top = 1000;*/
  mBounds.left = 1440;
  mBounds.top = 900;
  
  m.window.setTopLeftBounds(mBounds);

)

(
  var libDir = "/Users/colin/Projects/sounds/cs-supercollider-lib/";

  GUI.qt;
  Instr.dir = libDir ++ "Instr/";
  Instr.loadAll();
  /*Platform.userExtensionDir("/Users/colin/Projects/sounds/cs-supercollider-lib");*/
)

(

  ~performanceEnvironment = PerformanceEnvironment.new();
 
  true;
  
  /*Tempo.default.gui;*/
)

Tempo.bpm;
MIDISyncClock.tempo();

Instr("synths.DubBass").argsAndIndices().at(\rateMultiplier)

/* ControlSpec tests */
(
  var test;

  test = NoLagControlSpec((1/64), 16, \lin, step: (1/32));
  test.map((0..127).normalize);

  test = ControlSpec(0.001, 1.0, \exponential);

  "test:".postln;
  test.postln;

  "test.map(0.5):".postln;
  test.map(0.5).postln;

  nil

)

MIDIClient.sources.indexOf(MIDIIn.findPort("(out) To SuperCollider", "(out) To SuperCollider"))

/**
 *  Circular Buffer experiment
 **/
(
  var buf = Buffer.alloc(s, s.sampleRate * 2.0, 1),
    inputGroup = Group.new(),
    outputGroup = Group.after(inputGroup),
    inPatch,
    outPatch;

  Instr("CircularInput", {
    arg buf;

    var input,
      feedback = 0.5;

    input = SoundIn.ar(0);
    RecordBuf.ar(input,
      buf,
      preLevel: feedback,
      recLevel: 1.0 - feedback
    );

    // outputs nothing
    Silence.ar();
    
  }, [
    \buffer
  ]);

  Instr("CircularPlayback", {
    arg gate = 1,
      outBus = 0,
      buf = 0;

    var out,
      outEnv,
      chain;

    out = PlayBuf.ar(1, buf, BufRateScale.kr(buf) * MouseX.kr(0.1, 5.0), loop: -1);

    chain = FFT(LocalBuf(2048), out);
    chain = PV_BinScramble(chain, MouseY.kr, 0.1, MouseY.kr > 0.1);

    out = IFFT(chain);

    outEnv = EnvGen.kr(Env.cutoff(2, 1.0), gate, doneAction: 2);
    out = out * outEnv;

    [out, out];
  }, [
    \gate,
    \bus,
    \buffer
  ]);

  inPatch = Patch(Instr.at("CircularInput"), (
    buf: buf
  ));
  outPatch = Patch(Instr.at("CircularPlayback"), (
    buf: buf,
    gate: 1
  ));

  inPatch.play(group: inputGroup);
  outPatch.play(group: outputGroup);
)


(
  {
    var input = SoundIn.ar(1);
    Out.ar(0, input);
  }.play();
)

\buffer.asSpec()

'test'.isSymbol()


(
  var one,
    two;

  Instr("test-fm", {
    arg freqOffset = 0,
      freq,
      amp;

    var carrier,
      modulator,
      out,
      /*freq = MouseX.kr(280, 350).poll(10, "freq"),*/
      modFreq = freq * 2.0,
      /*modIndex = MouseY.kr(0.8, 1.2).poll(10, "modIndex"),*/
      modIndex = 1.07,
      /*modIndex = IRand.new(0.8, 1.2),*/
      lfo1,
      lfo2;
 
    lfo1 = SinOsc.kr(0.5).range(freq, freq + 0.25);
    lfo2 = SinOsc.kr(0.5).range(modIndex, modIndex + 0.01);
    modulator = SinOsc.ar(lfo1 * lfo2);
    carrier = SinOsc.ar(lfo1 + (modulator * 666) + freqOffset);
  
    out = amp * carrier;

    /*out = BPeakEQ.ar(
      out,
      freq: MouseX.kr(500, 15000),
      rq: MouseY.kr(0.001, 50),
      db: -12
    );*/

    /*out = MoogFF.ar(out,
      freq: MouseX.kr(500, 1000),
      gain: -6
    );*/

    /*out = out + AllpassC.ar(out, delayTime: 0.01);*/
    
    [out, out];

  });
  
  Instr("scary-fm-attempt", {
    arg numHarms = 3,
      amp = 0.9;

    /*var baseFreq = IRand.new(280, 350),*/
    var baseFreq = IRand.new(280, 350),
      i = 1,
      freq,
      outL = Silence.ar(),
      outR = Silence.ar(),
      mix,
      filterMix;

    (numHarms.asInteger).do({

      freq = baseFreq * (i + IRand.new(0.05, 0.15));
      outL = outL + Instr.ar("test-fm", (
        freq: freq,
        amp: (0.75 / numHarms)
      ));
      outR = outR + Instr.ar("test-fm", (
        freq: freq + 0.5,
        amp: (0.75 / numHarms)
      ));

      i = i + 1;
      
    });

    mix = Pan2.ar(outL, -0.75) + Pan2.ar(outR, 0.75);

    filterMix = 0.8;
    mix = ((1 - filterMix) * mix) + (filterMix * BLowPass.ar(mix,
      freq: SinOsc.kr(0.5).range(300, 440),
      rq: 0.1
    ));

    mix = amp * mix;

  });

  Patch(Instr.at("scary-fm-attempt"), (
    numHarms: 15
  )).play();

  /*one = Patch(Instr.at("test-fm"), (
    freqOffset: 0
  ));
  two = Patch(Instr.at("test-fm"), (
    freqOffset: 0.25
  ));*/

  /*one.play();
  two.play();
*/
  
)

(
  var m, mBounds;
  /*s.options.inDevice = "PreSonus FIREPOD (2112)";*/
  s.options.outDevice= "Soundflower (64ch)";
  s.quit;
  s.boot();
  //s.latency = 0;
  m = s.meter();

  // move level meter to bottom right of screen
  mBounds = m.window.bounds;
  /*mBounds.left = 1680;
  mBounds.top = 1000;*/
  mBounds.left = 1440;
  mBounds.top = 900;
  
  m.window.setTopLeftBounds(mBounds);
  
  //m = FreqScope.new(400, 200);
  mBounds.top = mBounds.top - 250;
  //m.window.setTopLeftBounds(mBounds);


  s.doWhenBooted({
    var test,
      sfxRoot = "/Volumes/Secondary/Samples/Recorded Sounds/Sound Effects/";
    
    Instr.dir = "/Users/colin/Projects/cs-supercollider-lib/Instr/";
    Instr.loadAll();

    //test = Patch("cs.fm.Kick.SoftKick").play();

    Buffer.read(Server.default, sfxRoot ++ "harp-plucks-04_long.aif", action: {
      arg buf;

      test = Patch("cs.sfx.PlayBufSegment", (
        buf: buf,
        gate: 1
      ));
    
      test.play();
    });
    
  });

)

/** Simple data structures **/
(
  var testList;

  testList = [];

  testList.add(5);

  testList.add(10);
)

(
  var testDict = ();

  "testDict[\nothere]:".postln;
  testDict[\nothere].postln;
)


({
  s.quit();
  /*s.options.inDevice = "PreSonus FIREPOD (2112)";*/
  s.options.outDevice= "JackRouter";
  /*s.options.sampleRate = 48000;*/
  s.boot();
  s.meter();
  //FreqScope.new(400, 200);

  s.doWhenBooted({
    Instr.dir = "/Users/colin/Projects/cs-supercollider-lib/Instr/";
    Instr.loadAll();

    Instr("cs.percussion.Impulsive").miditest();
  });
}.value());

(s.queryAllNodes();)


