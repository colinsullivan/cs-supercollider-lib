Instr("cs.sfx.PlayBuf", {
  
  arg buf,
    gate = 1,
    attackTime = 1.0,
    releaseTime = 1.0;

  var out;

  out = PlayBuf.ar(buf.numChannels, buf, trigger: gate, doneAction: 2);
  out = out * Linen.kr(gate, attackTime, 1.0, releaseTime, 2);

}, [
  \buffer,
  \gate
]);
