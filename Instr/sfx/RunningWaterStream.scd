Instr("sfx.RunningWaterStream", {

  arg buffer,
    gate = 0,
    amp = 1.0,
    decimatorRate = 48000,
    lowPassFreq = 10000;

  var out, outEnvShape, outEnv;

  outEnvShape = Env.adsr(
    attackTime: 0.1,
    decayTime: 0.3,
    sustainLevel: 1.0,
    releaseTime: 1.0,
    peakLevel: 1.0
  );
  outEnvShape.releaseNode = 2;
  
  outEnv = EnvGen.ar(
    outEnvShape,
    gate: gate,
    doneAction: 2
  );

  out = PlayBuf.ar(
    numChannels: buffer.numChannels(),
    bufnum: buffer,
    rate: 0.5 * BufRateScale.kr(buffer),
    trigger: gate,
    loop: 1
  );
  out = Decimator.ar(out,
    rate: decimatorRate,
    bits: 8
  );
  /*out = SmoothDecimator.ar(out,
    rate: 5000,
    smoothing: 0.1
  );*/
  out = BLowPass.ar(out,
    freq: lowPassFreq,
    rq: 0.5
  );

  out = amp * (outEnv * out);

}, [
  \buffer.asSpec(),
  \gate.asSpec(),
  \amp.asSpec(),
  ControlSpec(1000, 48000, \exponential),
  ControlSpec(1000, 10000, \exponential)
]);
