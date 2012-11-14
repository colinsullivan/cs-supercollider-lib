/**
 *  This patch takes a buffer and input channel and continuously records
 *  to that buffer at the desired feedback level (ratio of new amplitude
 *  to old amplitude).
 **/
Instr("fx.Circular.Recorder", {
  arg buf,
    inChannel = 0,
    feedbackRatio = 0.5;

  var input;

  input = SoundIn.ar(inChannel);
  RecordBuf.ar(input,
    buf,
    preLevel: feedbackRatio,
    recLevel: 1.0 - feedbackRatio
  );

  // outputs nothing
  Silence.ar();

}, [
  \buffer,
  \bus,
  ControlSpec(0.0, 1.0, \linear)
]);

/**
 *  This patch plays the circular buffer that was likely recorded with a
 *  `fx.Circular.Recorder` instance.
 **/
Instr("fx.Circular.Player", {
  arg gate = 1,
    outBus = 0,
    buf = 0;

  var out,
    outEnv,
    chain;

  out = PlayBuf.ar(1, buf,
    BufRateScale.kr(buf) * MouseX.kr(0.1, 5.0), 
    loop: -1
  );


  outEnv = EnvGen.kr(Env.cutoff(2, 1.0), gate, doneAction: 2);
  out = out * outEnv;

  [out, out];
}, [
  \gate,
  \bus,
  \buffer
]);
