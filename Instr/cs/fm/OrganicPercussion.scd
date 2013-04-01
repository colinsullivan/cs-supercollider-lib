Instr("cs.fm.OrganicPercussion", {
  arg freq = 440,
    amp = 0.9,
    gate = 0,
    // attack resonance
    modFreq = 2000;

  var carrier,
    modulator,
    modulatorGain,
    carrierGain,
    carrierEnvShape,
    carrierEnv,
    modEnvShape,
    modEnv,
    velocity,
    out;

  velocity = Latch.kr(gate, gate);

  // amount of FM synthesis depends on velocity (percussive)
  modulatorGain = velocity.exprange(0.25, 25) * freq;
  
  modEnvShape = Env.new(
    [1.0,   0.0,    0.0 ],
    [   0.25,    0.75    ],
    [   -3.0,    0.00    ],
    releaseNode: nil
  );

  carrierEnvShape = Env.new(
    //initial attack             peak          exp. decay
    [0.0,   0.75,     0.9,      1.0,      0.9,      0.0 ],
    [   0.001,    0.1,     0.05,      0.05,   0.79      ],
    [0.0,   2.5,      -2.5,     2.5,      -2.5,   -5.5  ],
    releaseNode: nil
  );

  modEnv = EnvGen.kr(
    envelope: modEnvShape,
    gate: gate,
    timeScale: 0.05
  );

  modulator = modEnv * (modulatorGain * SinOsc.ar(modFreq));
  
  carrierEnv = EnvGen.kr(
    envelope: carrierEnvShape,
    gate: gate,
    timeScale: 0.25
  );

  carrierGain = velocity.exprange(0.25, 1.0);

  carrier = carrierEnv * carrierGain * SinOsc.ar(freq + modulator);

  // free synth when both carrier and modulator envelopes are done
  FreeSelf.kr(Done.kr(carrierEnv) + Done.kr(modEnv) - 1);

  out = amp * carrier;
}, [
  \freq,
  \amp,
  \gate,
  \freq
]);
