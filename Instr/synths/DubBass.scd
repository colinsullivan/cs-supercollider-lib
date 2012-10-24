/**
 *  Adapted from [Decimator Wobble Bass](http://www.phrontist.org/2010/06/decimator-wobble-bass/)
 **/
Instr("synths.DubBass", {
  arg freq = 440,
    freqlag,
    gate = 1,
    amp = 0.7,
    tempo = 2.5,
    rateMultiplier = 0.5;
  
  var out, wobble, rate,
    // It's not dubstep if it's not at an integer multiple of 140bpm.
    /*tempo = (140/60)/4,*/
    wobbleTempo = tempo * rateMultiplier;

  //portamento
  freq = Lag.kr(freq, freqlag);
  
  // Pitch changes are slewed, faster up than down
  /*freq = Slew.kr(freq, 300, 20);*/
  
  // Choose "wobble" tempo
  wobble = SinOsc.kr(freq: wobbleTempo, phase: 3 * pi / 2);
  
  // Tone to be filtered. Notice modulation of width param.
  out = VarSaw.ar(freq, width: wobble.range(0.2,0.8))
      // Subbass.
      + SinOsc.ar(freq/2, mul: wobble.range(0,1)).dup;
  // Sampling resolution is decreased in time with the wobble.
  out = Decimator.ar(
    in: out, 
    rate: 15000,
    bits: wobble.range(1.2, 8)
  );
  // Emulated Moog Low Pass.
  out = MoogLadder.ar(out,
    ffreq: wobble.range(freq,25000),
    res: wobble.range(0.03,0.2)
  ).dup;
  // Turn it down a bit to prevent clipping.
  /*out = out * 0.25;*/
  
  // Simple Envelope
  out = out * EnvGen.kr(Env.adsr(
    attackTime: 0.01,
    releaseTime: 0.5
  ), gate, doneAction: 2); 

  out = [
    amp * DelayC.ar(out, 1, wobble.range(0,0.0012)),
    amp * DelayC.ar(out, 1, wobble.range(0.0012,0))
  ];

}, [
  \freq,
  [0, 2],
  \gate,
  \amp,
  \tempo,
  [(1/64), 16.0, \lin]
]);
