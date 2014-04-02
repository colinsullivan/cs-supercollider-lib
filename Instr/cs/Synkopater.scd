Instr("cs.SynkopaterDelay.SynkopaterDelay", {
  arg bus,
    numChan,
    delaySecs = 0,
    feedbackCoefficient = 0.6,
    amp = 1.0;

  var in, delayOut, out, localFB;

  in = In.ar(bus, numChan);

  // feedback loop output
  localFB = LocalIn.ar(numChan);

  // output of delay
  delayOut = DelayC.ar(feedbackCoefficient * localFB, 16.0, delaySecs);
  
  // feedback loop input 
  LocalOut.ar(in + delayOut);

  // send to out
  out = amp * delayOut;
}, [
  \audiobus,
  ObjectSpec(1),
  ControlSpec(0.0, 8.0),
  \unipolar,
  \amp
]);
