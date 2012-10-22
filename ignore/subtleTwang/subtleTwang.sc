(SynthDef.new("colinsul-subtleTwang", {
  arg freq,
    gate;
  
  var out,
    percEnv,
    /*bloop,*/
    percEnvShape,
    tone,
    toneHarm,
    toneMod,
    toneEnvShape,
    toneEnv,
    toneFreq;

  percEnvShape = Env.perc(
    attackTime: 0.005,
    releaseTime: 0.5,
    curve: -4
  );
  percEnv = EnvGen.ar(percEnvShape);

  toneEnvShape = Env.new(
    curve: \exp,
    levels: [0.001, 1.0, 0.001, 0.0],
    times:  [0.005, 1.0, 0.001]
  );
  toneEnv = EnvGen.ar(toneEnvShape, gate, doneAction: 2);


  /*bloop = Pulse.ar(
    freq: percEnv * (freq * 2),
    width: 2.0
  );*/


  toneMod = SinOsc.ar(
    freq: freq * percEnv.range(2.0, 1.0)
  );
  tone = SinOsc.ar(
    freq: toneMod.range(2.0, 1) * freq
  );
  toneHarm = 0.25 * SinOsc.ar(
    freq: toneMod.range(2.0, 1) * (freq * 2)
  );


  Out.ar([0, 1], (toneEnv * (tone + toneHarm)));
}))
