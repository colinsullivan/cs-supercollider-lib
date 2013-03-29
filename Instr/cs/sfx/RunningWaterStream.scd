Instr("cs.sfx.RunningWaterStream", {

  arg buffer,
    gate = 0,
    amp = 1.0,
    decimatorRate = 48000,
    lowPassFreq = 10000;

  var out,
    outEnvShape,
    outEnv;

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

Instr("cs.sfx.RunningWaterStreamAutomated", {

  arg buffer,
    gate = 0,
    amp = 1.0,
    /**
     *  hell is a mapping of both the decimator and the lowpass freq, nice
     *  effect on the running water sound.  These are the min and max
     *  values between which the `hell` value is oscillating.
     **/
    hellMin = 0.0,
    hellMax = 0.0,
    /**
     *  Frequency at which hell is changing
     **/
    hellFreq = 0,
    /**
     *  Bus to return the hellValue on for display.
     **/
    hellValueBus,
    /**
     *  Wether or not to use the oscillator.  If 0, hellMax is used as the
     *  hellValue.
     **/
    useOscillator = 0;


  /**
   *  Actual hell value after oscillation
   **/
  var hell = SinOsc.ar(hellFreq).range(hellMin, hellMax),
    runningWater = Instr("cs.sfx.RunningWaterStream"),
    decimatorRateSpec = runningWater.specs.at(
      runningWater.argsAndIndices().at('decimatorRate')
    ),
    lowPassFreqSpec = runningWater.specs.at(
      runningWater.argsAndIndices().at('lowPassFreq')
    ),
    inverseHell,
    out;

  // if useOscillator is high, use oscillator, if low, use hellMax
  hell = (useOscillator * hell) + ((1.0 - useOscillator) * hellMax);

  inverseHell = 1.0 - hell;
  
  Out.kr(hellValueBus, hell);
  
  out = Instr.ar("cs.sfx.RunningWaterStream", (
    buffer: buffer,
    gate: gate,
    amp: amp,
    decimatorRate: decimatorRateSpec.map(inverseHell),
    lowPassFreq: lowPassFreqSpec.map(inverseHell)
  ));

  out;

}, [
  \buffer.asSpec(),
  \gate.asSpec(),
  \amp.asSpec(),
  ControlSpec(0.0, 1.0, \linear),
  ControlSpec(0.0, 1.0, \linear),
  \beats.asSpec(),
  \controlbus.asSpec(),
  \gate.asSpec()
]);
