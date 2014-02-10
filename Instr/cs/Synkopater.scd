Instr("cs.Synkopater.SynkopaterVoice", {
  arg gate, delayAmt = 0;

  var out;

  out = Instr.ar("cs.percussion.Impulsive", (
    gate: gate,
    doneAction: 0
  ));


}, [
  \gate,
  ControlSpec(0.0, 10.0)
]);
