/**
 *  Adapted from [Decimator Wobble Bass](http://www.phrontist.org/2010/06/decimator-wobble-bass/)
 **/
Instr("cs.synths.DubBass", {
  arg freq = 440,
    freqlag,
    gate = 1,
    amp = 0.7,
    wobbleTempo = 2.5,
    rateMultiplier = 16,
    noteDur = 0;
  
  var out, wobble, rate;

  // extra sustain block for using in patterns
  if (noteDur != 0, {
    gate = Trig.kr(gate, noteDur);
  });

  gate.poll();
  //delta.poll();
  //Poll(Impulse.kr(10), sustain, "dubbass sustain: ");
  //Poll(Impulse.kr(10), dur, "dubbass dur: ");
  //Poll(Impulse.kr(10), gate, "dubbass gate: ");
  //Poll(Impulse.kr(10), delta, "dubbass delta: ");

  //portamento
  freq = Lag.kr(freq, freqlag);
  
  // Pitch changes are slewed, faster up than down
  /*freq = Slew.kr(freq, 300, 20);*/
  
  // Choose "wobble" wobbleTempo
  wobble = SinOsc.kr(freq: wobbleTempo * rateMultiplier, phase: 3 * pi / 2);
  
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
  [0.001, 100, \lin],
  [(1/64), 16.0, \lin]
]);
