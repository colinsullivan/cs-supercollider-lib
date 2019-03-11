Instr("cs.utility.EnvToBus", {
  arg env, bus, gate;
  var envgen;

  envgen = EnvGen.kr(env, gate, doneAction: Done.freeSelf);

  ReplaceOut.kr(bus, envgen);
}, [
  \env,
  \bus,
  \gate
]);
