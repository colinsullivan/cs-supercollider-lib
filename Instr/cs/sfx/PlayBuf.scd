Instr("cs.sfx.PlayBuf", {
  
  arg buf,
    gate = 1,
    playbackRate,
    startTime,
    attackTime,
    releaseTime,
    convertToStereo = 0;

  var out;

  out = PlayBuf.ar(
    buf.numChannels,
    buf,
    playbackRate * BufRateScale.kr(buf),
    gate,
    startTime * buf.sampleRate * buf.numChannels
  );
  out = out * Linen.kr(gate, attackTime, 1.0, releaseTime, 2);

  if (convertToStereo == 1, {
    out = [out, out];    
  });

  out;

}, [
  \buffer,
  \gate,
  ObjectSpec(1.0),
  ObjectSpec(0.0),
  ObjectSpec(1.0),
  ObjectSpec(1.0)
]);
