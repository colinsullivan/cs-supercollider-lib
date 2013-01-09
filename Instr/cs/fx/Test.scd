Instr("cs.fx.Test", {
  arg bus, numChan, mix = 1.0;

  var in, out, mod;

  in = In.ar(bus, numChan);
  mod = SinOsc.kr(0.1).range(150, 5000);
  out = LPF.ar(in, mod);

  (in * (1 - mix).sqrt + out * mix.sqrt);
}, [
  \audiobus,
  ObjectSpec(1),
  \unipolar
]);
