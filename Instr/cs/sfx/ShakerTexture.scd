Instr("cs.sfx.ShakerTexture.ShakerTexture", {
  arg gate = 0,
    amp = 1,
    shakersSegBuf,
    lowerResonantShakersSegBuf,
    grainRate,
    trigRate;

  var out,
    shakers,
    lowerResonantShakers,
    trig;

  trig = Dust.kr(trigRate);
  
  shakers = Instr.ar("cs.sfx.ShakerTexture.ShakerTextureSingle", (
    gate: gate,
    amp: amp,
    sndbuf: shakersSegBuf,
    grainRate: grainRate,
    trig: trig
  ));

  lowerResonantShakers = Instr.ar("cs.sfx.ShakerTexture.ShakerTextureSingle", (
    gate: gate,
    amp: amp * -6.0.dbamp(),
    sndbuf: lowerResonantShakersSegBuf,
    grainRate: grainRate,
    trig: trig
  ));

  out = shakers + lowerResonantShakers;

  out;

}, [
  \gate,
  \amp,
  \buffer,
  \buffer,
  ControlSpec(-2.0, 2.0),
  ControlSpec(0.01, 10)
]);

Instr("cs.sfx.ShakerTexture.ShakerTextureSingle", {
  arg gate = 0,
    amp = 1,
    sndbuf,
    grainRate,
    trigRate,
    trig = 0;

  var env,
    freqdev,
    grain,
    out;

  if (trig == 0, {
    trig = Dust.kr(trigRate);
  });

  env = EnvGen.kr(
    Env([0, 1, 0], [1, 1], \sin, 1),
    gate,
    //doneAction: Done.freeSelf
    doneAction: 2
  );
  grain = GrainBuf.ar(
    2,
    trigger: trig,
    dur: LFNoise1.kr.range(0.001, 0.8),
    //dur: MouseY.kr(0.001, 0.75),
    sndbuf: sndbuf,
    rate: grainRate,
    //rate: LFNoise1.kr.range(0.1, 2.0),
    pos: LFNoise2.kr(0.1).range(0, 1),
    interp: 4,
    pan: 0,
  );
  out = grain * env * amp;
}, [
  \gate,
  \amp,
  \buffer,
  ControlSpec(-0.5, 2.0),
  ControlSpec(0.01, 10)
]);
