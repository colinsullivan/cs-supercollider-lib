Instr("cs.fm.OrganicPercussion", {
  arg freq = 440,
    amp = 0.9,
    gate = 0,
    // attack resonance
    modFreq = 2400,
    // silly sin pitch bend
    doFreqSweep = 0,
    freqSweepTargetMultiplier = 1.0,
    // duration of notes can be determined by velocity or set manually
    autoDurationOn = 1,
    noteDuration = 1.0,
    toneModulatorGainMultiplier = 0.0;

  var carrier,
    modulator,
    modulatorGain,
    carrierGain,
    carrierEnvShape,
    carrierEnv,
    modEnvShape,
    modEnv,
    velocity,
    out,
    freqSweepEnvShape,
    freqSweepEnv,
    sustainTime,
    toneModulatorGain,
    toneModulator;

  velocity = Latch.kr(gate, gate);

  sustainTime = (
    autoDurationOn * velocity.exprange(0.1, 1.5)
    + ((1.0 - autoDurationOn) * noteDuration)
  );

  freqSweepEnvShape = Env.new([freq, freq * freqSweepTargetMultiplier], [1.0], \exp);
  freqSweepEnv = EnvGen.kr(
    envelope: freqSweepEnvShape,
    gate: gate,
    timeScale: sustainTime
  );
  // choose between frequency sweep and non frequency sweep
  freq = (doFreqSweep * freqSweepEnv) + ((1.0 - doFreqSweep) * freq);

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
    timeScale: 0.08
  );

  modulator = modEnv * (modulatorGain * SinOsc.ar(modFreq));
  
  toneModulatorGain = toneModulatorGainMultiplier * freq;
  toneModulator = toneModulatorGain * (
    SinOsc.ar(freq * 2.0)/* + SinOsc.ar(freq * 2.0)*/
  );
  
  carrierEnv = EnvGen.kr(
    envelope: carrierEnvShape,
    gate: gate,
    timeScale: sustainTime
  );

  carrierGain = velocity.exprange(0.25, 1.0);

  carrier = carrierEnv * carrierGain * SinOsc.ar(freq + modulator + toneModulator);

  // free synth when both carrier and modulator envelopes are done
  FreeSelf.kr(Done.kr(carrierEnv) + Done.kr(modEnv) - 1);

  out = amp * carrier;

  out = [out, out];
}, [
  \freq,
  \amp,
  \gate,
  // modFreq
  \freq,
  // doFreqSweep
  \unipolar,
  // freqSweepTargetMultiplier
  ControlSpec(0.01, 10, \exp),
  // autoDurationOn
  \unipolar,
  // noteDuration
  ControlSpec(0.1, 10),
  // toneModulatorGainMultiplier
  ControlSpec(0.0, 20.0)
]);
