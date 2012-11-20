Instr("util.EnvelopedWhiteNoise", {
  arg gate = 0,
    amp = 0.25;

  var outEnv,
    outEnvShape,
    out;

  outEnvShape = Env.new(
    levels: [0,   1,    1,    0 ],
    times:  [ 1,    1,    1     ],
    curve:  [ -4,    0,    4     ]
  );
  outEnvShape.plot();

  outEnv = EnvGen.ar(outEnvShape, gate: gate, doneAction: 2);

  out = amp * (WhiteNoise.ar() * outEnv);

}, [
  \gate,
  \amp
]);
