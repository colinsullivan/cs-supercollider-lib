Instr("cs.sfx.PlayBufWarped", {
  
  arg buf,
    gate = 1,
    playbackRate,
    startTime,
    attackTime,
    releaseTime,
    convertToStereo = 0,
    warpAmp = 0.5,
    warpFreq = 0.5,
    /**
     *  When set, will take a dive when gate is turned off.  Value
     *  is used as a multiplier to the playback rate.
     *
     *  ex. 0.5 will dive to 50% speed
     *      1.5 will dive to 150% speed
     */
    pitchDive = 1;

  var out,
    rateModulator,
    pitchDiveEnvelope,
    pitchDiveEnvelopeShape;


  rateModulator = SinOsc.ar(warpFreq).range(
    playbackRate - warpAmp,
    playbackRate + warpAmp
  );

  pitchDiveEnvelopeShape = Env.new(
    [1.0,       1.0,              pitchDive ],
    [   attackTime,   releaseTime           ],
    \exp,
    releaseNode: 1
  );

  pitchDiveEnvelope = EnvGen.kr(pitchDiveEnvelopeShape, gate);


  out = Instr.ar("cs.sfx.PlayBuf", (
    buf: buf,
    gate: gate,
    playbackRate: rateModulator * pitchDiveEnvelope,
    startTime: startTime,
    attackTime: attackTime,
    releaseTime: releaseTime,
    convertToStereo: convertToStereo
  ));

}, [
  \buffer,
  \gate,
  ObjectSpec(1.0),
  ObjectSpec(0.0),
  ObjectSpec(1.0),
  ObjectSpec(1.0),
  ObjectSpec(0.0),
  \amp,
  \beats,
  ObjectSpec(1.0)
]);
