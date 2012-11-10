Instr("fm.Lazers", {
  arg freq = 440,
    amp = 1.0,
    gate = 1,
    modIndex = 2.5,
    mod2Index = 0.2;
  
  // cool downward twang
  /*modIndex = 2.5;
  mod2Index = 0.2;*/

  // downward chirp
  /*modIndex = 0.5;
  mod2Index = 0.2;*/

  // rich and dirty
  /*modIndex = 0.76338;
  mod2Index = 2.97439;*/

  // nice twang
  /*modIndex = 0.493666;
  mod2Index = 3.8497;*/

  // farty, vintage sounding 
  /*modIndex = 2.39887;
  mod2Index = 0.438485;*/

  // high and buzzy
  /*modIndex = 1.49069;
  mod2Index = 1.52105;*/

  // vintage chirp
  /*modIndex = 1.12004;
  mod2Index = 0.254175;*/

  // lazer
  /*modIndex = 0.0254813;
  mod2Index = 1.37289;*/

  // bubble pop
  /*modIndex = 3.79869;
  mod2Index = 0.0229534;*/

  // random
  /*modIndex = ExpRand.new(0.01, 6);
  mod2Index = ExpRand.new(0.02, 6);*/

  var carrierEnvShape,
    carrierEnv,
    carrier,
    modEnvShape,
    modEnv,
    mod2EnvShape,
    mod2Env,
    mod,
    mod2,
    out,
    trigGate = Trig1.ar(gate, 0.01);

  carrierEnvShape = Env.adsr(
    attackTime: 0.1,
    decayTime: 0.1,
    sustainLevel: 0.9,
    releaseTime: 0.2,
    curve: -4
  );

  modEnvShape = Env.adsr(
    attackTime: 0.05,
    decayTime: 0.5,
    sustainLevel: 0.7,
    releaseTime: 1.5,
    curve: -10
  );

  modEnv = EnvGen.kr(
    envelope: modEnvShape,
    gate: trigGate
  );
  
  mod2EnvShape = Env.adsr(
    attackTime: 0.05,
    decayTime: 0.5,
    sustainLevel: 0.7,
    releaseTime: 1.5,
    curve: -10
  );

  mod2Env = EnvGen.kr(
    envelope: mod2EnvShape,
    gate: trigGate
  );


  mod = modEnv * LFPar.ar(modIndex * freq * LocalIn.ar(1));
  LocalOut.ar(mod);

  mod2 = mod2Env * LFSaw.ar(mod2Index * freq * mod);

  /*carrierEnvShape = Env.adsr();*/

  /*carrierEnvShape = Env.asr();*/

  carrierEnv = EnvGen.kr(
    envelope: carrierEnvShape,
    gate: trigGate
  );

  carrier = carrierEnv * SinOsc.ar(freq + (mod2 * freq * 100));

  out = amp * carrier;

  // take out some of that piercing.
  out = BPeakEQ.ar(out,
    freq: 22000,
    rq: 5,
    db: -16
  );

  FreeSelf.kr(Done.kr(carrierEnv) + Done.kr(modEnv) + Done.kr(mod2Env) - 2);

  [out, out];

});
