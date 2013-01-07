Instr("cs.fx.Test", {
  arg outbus, mix;

  var in, out, mod;

  in = In.ar(outbus, 2);
  mod = SinOsc.kr(0.1).range(150, 5000);
  out = [LPF.ar(in[0], mod), LPF.ar(in[1], mod)];

  ReplaceOut.ar(outbus, in * (1 - mix).sqrt + out * mix.sqrt);
}, [
  \audiobus.asSpec(),
  \unipolar
], MultiTrackAudioSpec());
