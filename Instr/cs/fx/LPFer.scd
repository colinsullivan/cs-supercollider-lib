
Instr("cs.fx.LPFer", {
  arg bus, numChan, cutoffFreq = 100;

  var out, in;
  
  in = In.ar(bus, numChan);

  out = LPF.ar(in, cutoffFreq);

}, [
  \audiobus,
  ObjectSpec(1),
  \freq
]);

Instr("cs.fx.LPFerBus", {

  arg bus, numChan, cutoffFreq = 100;
  var out, in;

  in = In.ar(bus, numChan);

  out = Instr.ar("cs.fx.LPFer", (
    in: in,
    cutoffFreq: cutoffFreq
  ));
});

Instr("cs.fx.LPFerModulated", {

  arg bus, numChan, cutoffMinFreq, cutoffMaxFreq, cutoffModFreq, cutoffModPhase = 0;

  var out;

  out = Instr.ar("cs.fx.LPFer", (
    bus: bus,
    numChan: numChan,
    cutoffFreq: SinOsc.ar(cutoffModFreq).range(cutoffMinFreq, cutoffMaxFreq)
  ));

}, [
  \audiobus,
  ObjectSpec(1),
  \freq,
  \freq,
  \freq,
  \phase
]);
