Instr("fm.Kick", {
  arg freq = 55,
    amp = 1.0,
    gate = 0;

  var carrierEnvShape,
    carrierEnv,
    carrier,
    modEnvShape,
    modEnv,
    mod,
    out,
    trigGate = Trig1.ar(gate, 0.01);

  carrierEnvShape = Env.adsr(
    attackTime: 0.05,
    decayTime: 0.8,
    sustainLevel: 0.9,
    releaseTime: 1.3,
    curve: -4
  );
  /*carrierEnvShape.releaseNode = nil;*/
  /*carrierEnvShape = Env.new(
    levels: [0,   1,  0.9,  0],
    times:  [0.05,    0.8,  1.3],
    curve:  -4
  );*/

  modEnvShape = Env.adsr(
    attackTime: 0.05,
    decayTime: 0.5,
    sustainLevel: 0.6,
    releaseTime: 0.2,
    curve: -10
  );
  /*modEnvShape.releaseNode = nil;*/

  /*modEnvShape = Env.new(
    levels: [0,   1,  0.7,  0],
    times:  [0.05,    0.5,  0.2],
    curve:  -10
  );*/

  modEnv = EnvGen.kr(
    envelope: modEnvShape,
    gate: trigGate
  );

  mod = modEnv * LFPar.ar(0.5 * freq);

  /*carrierEnvShape = Env.adsr();*/

  /*carrierEnvShape = Env.asr();*/

  carrierEnv = EnvGen.kr(
    envelope: carrierEnvShape,
    gate: trigGate
  );

  carrier = carrierEnv * SinOsc.ar(freq + (mod * freq * 20));

  out = amp * carrier;

  FreeSelf.kr(Done.kr(carrierEnv) + Done.kr(modEnv) - 1);

  [out, out];

});
