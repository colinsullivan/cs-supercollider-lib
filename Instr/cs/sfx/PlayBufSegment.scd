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

  var out, bufnum;

  if (buf.class == BufferProxy, {
    bufnum = buf.bufnumIr    
  }, {
    bufnum = buf.bufnum
  });

  out = gain * PlayBuf.ar(
    buf.numChannels,
    bufnum,
    playbackRate * BufRateScale.kr(bufnum),
    gate,
    (startTime / BufDur.kr(bufnum)) * BufFrames.kr(bufnum)
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
  ControlSpec(-10, 10),
  // startTime
  ControlSpec(0, 9999999999),
  // attackTime
  ControlSpec(0.01, 4.0),
  // endTime (default plays one second of soundfile)
  ControlSpec(0, 99999999999),
  \gain,
  // releaseTime
  ControlSpec(0.01, 4.0)
]);
