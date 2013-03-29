Instr("cs.sfx.PlayBufSegment", {
  
  arg buf,
    gate = 1,
    playbackRate,
    startTime,
    attackTime,
    endTime,
    releaseTime,
    gain,
    convertToStereo = 0;

  var out;

  out = gain * PlayBuf.ar(
    buf.numChannels,
    buf,
    playbackRate * BufRateScale.kr(buf),
    gate,
    startTime * buf.sampleRate * buf.numChannels
  );

  out = out * EnvGen.ar(
    Env.linen(attackTime, (endTime - startTime), releaseTime),
    gate,
    doneAction: 2
  );

  if (convertToStereo == 1, {
    out = [out, out];    
  });

  out;

}, [
  \buffer,
  \gate,
  // playbackRate
  ObjectSpec(1.0),
  // startTime
  ObjectSpec(0.0),
  // attackTime
  ObjectSpec(1.0),
  // endTime (default plays one second of soundfile)
  ObjectSpec(1.0),
  \gain,
  // releaseTime
  ObjectSpec(1.0)
]);
