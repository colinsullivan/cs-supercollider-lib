Instr("cs.fx.HPFer", {
  arg bus, numChan, cutoffFreq = 100;

  var out, in;
  
  in = In.ar(bus, numChan);

  out = HPF.ar(in, cutoffFreq);

}, [
  \audiobus,
  ObjectSpec(1),
  \freq
]);

Instr("cs.fx.HPFerModulated", {

  arg bus, numChan, cutoffMinFreq, cutoffMaxFreq, cutoffModFreq, cutoffModPhase = 0;

  var out;

  out = Instr.ar("cs.fx.HPFer", (
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
