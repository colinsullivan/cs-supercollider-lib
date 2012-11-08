Instr("fm.Kick", {
  arg freq = 55,
    amp = 1.0,
    gate = 1;

  var carrierEnvShape,
    carrierEnv,
    carrier,
    modEnvShape,
    modEnv,
    mod,
    out;

  carrierEnvShape = Env.adsr(
    attackTime: 0.05,
    decayTime: 0.8,
    sustainLevel: 0.9,
    releaseTime: 1.3,
    curve: -4
  );

  modEnvShape = Env.adsr(
    attackTime: 0.05,
    decayTime: 0.5,
    sustainLevel: 0.7,
    releaseTime: 0.2,
    curve: -10
  );

  modEnv = EnvGen.kr(
    envelope: modEnvShape,
    gate: gate
  );

  mod = modEnv * LFPar.ar(0.5 * freq);

  /*carrierEnvShape = Env.adsr();*/

  /*carrierEnvShape = Env.asr();*/

  carrierEnv = EnvGen.kr(
    envelope: carrierEnvShape,
    gate: gate
  );

  carrier = carrierEnv * SinOsc.ar(freq + (mod * freq * 20));

  out = amp * carrier;

  FreeSelf.kr(Done.kr(carrierEnv) + Done.kr(modEnv) - 1);

  [out, out];

});
