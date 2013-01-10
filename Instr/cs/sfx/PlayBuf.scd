Instr("cs.sfx.PlayBuf", {
  
  arg buf,
    gate = 1,
    startTime = 0.0,
    attackTime = 1.0,
    releaseTime = 1.0,
    convertToStereo = 0,
    rate = 1;

  var out;

  out = PlayBuf.ar(
    buf.numChannels,
    buf,
    rate * BufRateScale.kr(buf),
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
  \gate
]);
