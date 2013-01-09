Instr("cs.sfx.PlayBuf", {
  
  arg buf,
    gate = 1,
    startTime = 0.0,
    attackTime = 1.0,
    releaseTime = 1.0,
    convertToStereo = 0;

  var out;

  out = PlayBuf.ar(buf.numChannels, buf, trigger: gate, startPos: startTime);
  out = out * Linen.kr(gate, attackTime, 1.0, releaseTime, 2);

  if (convertToStereo == 1, {
    out = [out, out];    
  });

  out;

}, [
  \buffer,
  \gate
]);
