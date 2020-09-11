Instr("cs.fm.WideBass", {
  arg freq = 440,
    gate = 1,
    freqlag = 0,
    pb = 1.0,
    amp = 0.9,
    // attack resonance
    attackModFreq = 100,
    toneModulatorGainMultiplier = 0.0,
    toneModulatorLFOAmount = 0.0,
    toneModulatorLFORate = 0.0,
    useSustain = 0,
    sustainTime = 0,
    useModulatorBus = 0,
    toneModulatorGainMultiplierBus,
    toneModulatorLFOAmountBus;

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
    out,
    toneModulatorLFO;

  //portamento
  freq = Lag.kr(freq, freqlag);

  // pitch bend
  freq = pb * freq;


  if (useSustain != 0, {
    gate = Trig.kr(gate, sustainTime); 
  });

  velocity = Latch.kr(gate, gate);

  // amount of FM synthesis depends on velocity (percussive)
  attackModulatorGain = velocity.exprange(0.25, 25) * freq;

  if (useModulatorBus != 0, {
    toneModulatorGainMultiplier = 0.1 + In.kr(toneModulatorGainMultiplierBus) * (18.0 - 0.1);
  });
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
    [   0.005,    0.2,     0.05,      0.05,   0.15      ],
    [0.0,   2.5,      -2.5,     2.5,      -2.5,   0  ],
    releaseNode: 3
  );

  attackModEnv = EnvGen.kr(
    envelope: attackModEnvShape,
    gate: gate,
    timeScale: 0.25
  );

  attackModulator = attackModEnv * (attackModulatorGain * SinOsc.ar(attackModFreq));

  if (useModulatorBus != 0, {
    toneModulatorLFOAmount = 50 * In.kr(toneModulatorLFOAmountBus);
  });

  toneModulatorLFO = SinOsc.ar(toneModulatorLFORate, mul: toneModulatorLFOAmount);

  toneModulatorGain = toneModulatorGain + toneModulatorLFO;

  toneModulator = toneModulatorGain * (SinOsc.ar(freq * 2.0) + SinOsc.ar(freq * 4.0));

  carrierEnv = EnvGen.kr(
    envelope: carrierEnvShape,
    gate: gate,
    doneAction: Done.freeSelf
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
  //Done.kr(carrierEnv).poll(label: "carrierEnv done");
  //Done.kr(attackModEnv).poll(label: "attackModEnv done");
  //FreeSelf.kr(Done.kr(carrierEnv) + Done.kr(attackModEnv) - 1);

  out = amp * out;
}, [
  \freq,
  \gate,
  [0, 2],
  [1.0 / 4.0, 4.0],
  \amp,
  // attackModFreq
  \freq,
  // toneModulatorGainMultiplier
  [0.1, 20.0],
  // tone modulator LFO amount
  [0, 50.0],
  // tone modulator LFO rate
  \beats,
  // useSustain
  StaticIntegerSpec(0, 1),
  // sustainTime
  [0, 10],
  // useModulatorBus
  StaticIntegerSpec(0, 1),
  // toneModulatorGainMultiplierBus
  \controlbus,
  // toneModulatorLFOAmountBus
  \controlbus
]);
