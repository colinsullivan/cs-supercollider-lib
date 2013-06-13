Instr("cs.fm.WideBass", {
  arg freq = 440,
    freqlag,
    pb = 1,
    amp = 0.9,
    gate = 0,
    // attack resonance
    attackModFreq = 100,
    toneModulatorGainMultiplier = 0.0;

  var carrier,
    attackModulator,
    attackModulatorGain,
    attackModEnvShape,
    attackModEnv,
    toneModulator,
    toneModulatorGain,
    carrierGain,
    carrierEnvShape,
    carrierEnv,
    velocity,
    outL,
    outR,
    outPreFiltered,
    out;

  //portamento
  freq = Lag.kr(freq, freqlag);

  // pitch bend
  freq = pb * freq;
  
  velocity = Latch.kr(gate, gate);

  // amount of FM synthesis depends on velocity (percussive)
  attackModulatorGain = velocity.exprange(0.25, 25) * freq;

  toneModulatorGain = toneModulatorGainMultiplier * freq;
  
  attackModEnvShape = Env.new(
    [1.0,   0.0,    0.0 ],
    [   0.25,    0.75    ],
    [   -3.0,    0.00    ],
    releaseNode: nil
  );

  carrierEnvShape = Env.new(
    //initial attack             peak          exp. decay
    [0.0,   0.75,     0.9,      1.0,      0.9,      0.0 ],
    [   0.001,    0.2,     0.05,      0.05,   0.3      ],
    [0.0,   2.5,      -2.5,     2.5,      -2.5,   -5.5  ],
    releaseNode: 3
  );

  attackModEnv = EnvGen.kr(
    envelope: attackModEnvShape,
    gate: gate,
    timeScale: 0.25
  );

  attackModulator = attackModEnv * (attackModulatorGain * SinOsc.ar(attackModFreq));

  toneModulator = toneModulatorGain * (SinOsc.ar(freq * 2.0) + SinOsc.ar(freq * 4.0));
  
  carrierEnv = EnvGen.kr(
    envelope: carrierEnvShape,
    gate: gate
  );

  carrierGain = velocity.exprange(0.25, 1.0);

  // FM tone
  outL = carrierEnv * carrierGain * SinOsc.ar(freq + attackModulator + toneModulator);
  // second FM tone slightly detuned
  /*outL = outL + (
    carrierEnv * carrierGain * SinOsc.ar(0.01 + freq + attackModulator + toneModulator)
  );*/

  // second channel is slightly delayed for width
  out = [outL, DelayC.ar(outL, 0.03, 0.03)];

  // bass resonances
  /*out = RLPF.ar(out, freq / 0.33, 1.2);*/
  /*out = RLPF.ar(out, freq / 0.66, 1.2);*/
  /*out = RLPF.ar(out, freq / 1.33, 0.9);*/
  /*out = RLPF.ar(out, freq / 1.66, 0.9);*/
  /*out = (-6.0.dbamp()) * out;*/
  /*out = RLPF.ar(out, 250, 0.5, 4.0.dbamp());*/

  // free synth when both carrier and modulator envelopes are done
  FreeSelf.kr(Done.kr(carrierEnv) + Done.kr(attackModEnv) - 1);

  out = amp * out;
}, [
  \freq,
  [0, 2],
  \nil,
  \amp,
  \gate,
  // attackModFreq
  \freq,
  // toneModulatorGainMultiplier
  ControlSpec(0.1, 20.0)
]);
