
Instr("cs.fx.LPFer", {
  arg in, cutoffFreq = 100;

  var out;

  out = LPF.ar(in, cutoffFreq);

});

Instr("cs.fx.LPFerModulated", {

  arg in, cutoffMinFreq, cutoffMaxFreq, cutoffModFreq;

  var out;

  out = Instr.ar("cs.fx.LPFer", (
    in: in,
    cutoffFreq: SinOsc.ar(cutoffModFreq).range(cutoffMinFreq, cutoffMaxFreq)
  ));

});
