Instr("cs.Synkopater.SynkopaterDelayQuadWithInput", {
  arg inputChannelNums,
    numChan,
    delaySecs = 1.0,
    feedbackCoefficient = 0.6,
    amp = 1.0;

  var in, delayOut, out, localFB;

  in = SoundIn.ar(inputChannelNums);

  // feedback loop output
  localFB = LocalIn.ar(numChan);

  // output of delay
  delayOut = DelayC.ar(
    feedbackCoefficient * localFB,
    16.0,
    // Subtracts block size to account for LocalIn / LocalOut latency
    // See: LocalIn docs
    delaySecs - ControlRate.ir.reciprocal
  );
  
  // feedback loop input 
  LocalOut.ar(in + delayOut);
  //delayOut = SwitchDelay.ar(
    //in,
    //drylevel: 0.0,
    //wetlevel: 1.0,
    //delaytime: delaySecs,
    //delayfactor: feedbackCoefficient
  //);

  // send to out
  out = amp * delayOut;

  [out[0], out[1], out[1], out[0]];
}, [
  ObjectSpec([1]),
  ObjectSpec(1),
  ControlSpec(0.0, 8.0),
  \unipolar,
  \amp
]);


Instr("cs.Synkopater.SynkopaterDelayWithInput", {
  arg inputChannelNums,
    numChan,
    delaySecs = 1.0,
    feedbackCoefficient = 0.6,
    amp = 1.0;

  var in, delayOut, out, localFB;

  in = SoundIn.ar(inputChannelNums);

  // feedback loop output
  localFB = LocalIn.ar(numChan);

  // output of delay
  delayOut = DelayC.ar(
    feedbackCoefficient * localFB,
    16.0,
    // Subtracts block size to account for LocalIn / LocalOut latency
    // See: LocalIn docs
    delaySecs - ControlRate.ir.reciprocal
  );
  
  // feedback loop input 
  LocalOut.ar(in + delayOut);
  //delayOut = SwitchDelay.ar(
    //in,
    //drylevel: 0.0,
    //wetlevel: 1.0,
    //delaytime: delaySecs,
    //delayfactor: feedbackCoefficient
  //);

  // send to out
  out = amp * delayOut;
}, [
  ObjectSpec([1]),
  ObjectSpec(1),
  ControlSpec(0.0, 8.0),
  \unipolar,
  \amp
]);

Instr("cs.Synkopater.SynkopaterDelay", {
  arg bus,
    numChan,
    delaySecs = 1.0,
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
  //delayOut = SwitchDelay.ar(
    //in,
    //drylevel: 0.0,
    //wetlevel: 1.0,
    //delaytime: delaySecs,
    //delayfactor: feedbackCoefficient
  //);

  // send to out
  out = amp * delayOut;
}, [
  \audiobus,
  ObjectSpec(1),
  ControlSpec(0.0, 8.0),
  \unipolar,
  \amp
]);
