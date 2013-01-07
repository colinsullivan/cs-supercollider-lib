Instr("cs.sfx.LoopBuf", {

  arg buf,
    gate = 1,
    attackTime = 1.0,
    releaseTime = 1.0;

  var out;

  out = Linen.kr(gate, attackTime, 1.0, releaseTime, 2);
  out = out * PlayBuf.ar(buf.numChannels, buf.bufnum, loop: 1);

}, [
  \buffer,
  \gate
]);
