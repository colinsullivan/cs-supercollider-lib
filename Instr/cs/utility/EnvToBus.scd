Instr("cs.utility.EnvToBus", {
  arg gate, bus, env;
  var envgen;

  envgen = EnvGen.kr(
    env,
    gate,
    doneAction: Done.freeSelf
  );

  ReplaceOut.kr(bus, envgen);
}, [
  \gate,
  \bus,
  \env
]);
