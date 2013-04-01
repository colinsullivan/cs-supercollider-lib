Instr("cs.synths.SmoothAndWideBass", {
  arg freq = 440,
    freqlag,
    gate = 1,
    amp = 0.7,
    tempo = 2.5,
    rateMultiplier = 16;
  
  var out, wobble, rate,
    // It's not dubstep if it's not at an integer multiple of 140bpm.
    /*tempo = (140/60)/4,*/
    wobbleTempo = tempo * rateMultiplier,
    carrierFreqRatio = 1.0,
    modulatorFreqRatio = 1.0,
    modulatorTwoFreqRatio = 1.0,
    // not sure if having these defaults here actually does anything.
    modEnvShape = Env.adsr(
      attackTime: 0.2,
      decayTime: 0.1,
      sustainLevel: 0.5 * amp,
      releaseTime: 0.1,
      peakLevel: amp,
      curve: [4, -4, -2]
    ),
    carrierEnvShape = Env.adsr(
      attackTime: 0.2,
      decayTime: 0.1,
      sustainLevel: 0.5 * amp,
      releaseTime: 0.1,
      peakLevel: amp,
      curve: [4, -4, -2]
    ),
    carrier,
    modulator,
    modulatorTwo,
    carrierEnv,
    modEnv;


  //portamento
  freq = Lag.kr(freq, freqlag);
  
  // Pitch changes are slewed, faster up than down
  /*freq = Slew.kr(freq, 300, 20);*/
  
  // Choose "wobble" tempo
  wobble = SinOsc.kr(freq: wobbleTempo);

  carrierFreqRatio = 2.0 * wobble.range((1.0/6.0), 6.0);
  modulatorFreqRatio = 1.0;
  //modulatorTwoFreqRatio = 8.0;

  modEnv = EnvGen.kr(
    envelope: modEnvShape,
    gate: gate
  );
  
  /*modulatorTwo = modEnv * SinOsc.ar(
    freq * modulatorTwoFreqRatio
  );*/
  
  modulator = modEnv * SinOsc.ar(
    freq * modulatorFreqRatio
  );

  // carrier sustains until noteoff
  carrierEnvShape.releaseNode = 2;

  carrierEnv = EnvGen.kr(
    envelope: carrierEnvShape,
    gate: gate
  );

  out = carrierEnv * SinOsc.ar(
    freq * carrierFreqRatio * modulator
  );

  //out = amp * carrier;
  
  //out = Instr.ar("cs.fm.BasicFM", (
    //freq: freq,
    //amp: amp,
    //gate: gate,
    //carrierFreqRatio: 1.0,
    //modulatorFreqRatio: wobble.range(32.0, 48.0)
  //));
  
  // Tone to be filtered. Notice modulation of width param.
  /*out = SinOsc.ar(freq + wobble.range())
      // Subbass.
      + SinOsc.ar(freq/2, mul: wobble.range(0,1)).dup;*/
  // Sampling resolution is decreased in time with the wobble.
  /*out = Decimator.ar(
    in: out, 
    rate: 15000,
    bits: wobble.range(1.2, 8)
  );*/
  // Emulated Moog Low Pass.
  /*out = MoogLadder.ar(out,
    ffreq: wobble.range(3000, 20000),
    res: 0.6
  );*/
  /*out = BPeakEQ.ar(out,
    freq: wobble.range(500, 2500),
    rq: 0.6,
    db: 7.0
  );*/
  // Turn it down a bit to prevent clipping.
  /*out = out * 0.25;*/
  
  // Simple Envelope
  /*out = out * EnvGen.kr(Env.adsr(
    attackTime: 0.01,
    releaseTime: 0.5
  ), gate, doneAction: 2); */

  // free synth when both carrier and modulator envelopes are done
  FreeSelf.kr(Done.kr(carrierEnv) + Done.kr(modEnv) - 1);

  out = [
    amp * DelayC.ar(out, 1, wobble.range(0,0.0012)),
    amp * DelayC.ar(out, 1, wobble.range(0,0.1))
  ];
  
}, [
  \freq,
  [0, 2],
  \gate,
  \amp,
  \tempo,
  [(1/64), 16.0, \lin]
]);


