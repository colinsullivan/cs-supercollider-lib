Instr("cs.Synkopater.SynkopaterDelay", {
  arg bus, numChan, delaySecs = 0, feedbackCoefficient = 0.6, amp = 0.5;

  var in, out, localFB;

  in = In.ar(bus, numChan);

  localFB = LocalIn.ar(numChan);

  out = DelayC.ar(in + localFB, 8.0, delaySecs);

  // input signal plus feedback
  LocalOut.ar(feedbackCoefficient * out);

  // send to out
  out = amp * out;
}, [
  \audiobus,
  ObjectSpec(1),
  ControlSpec(0.0, 8.0),
  \unipolar,
  \amp
]);
