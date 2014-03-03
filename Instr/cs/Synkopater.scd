Instr("cs.Synkopater.SynkopaterDelay", {
  arg bus, numChan, delaySecs = 0, feedbackCoefficient = 0.6;

  var in, out, localFB;

  in = In.ar(bus, numChan);

  localFB = LocalIn.ar(numChan);

  out = in + DelayN.ar(localFB, 8.0, delaySecs);

  // feed back in
  LocalOut.ar(feedbackCoefficient * out);

  // send to out
  out;
}, [
  \audiobus,
  ObjectSpec(1),
  ControlSpec(0.0, 8.0)
]);
