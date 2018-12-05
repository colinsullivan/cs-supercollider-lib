Instr("cs.sfx.LoopBuf", {

  arg buf,
    gate = 1,
    attackTime = 1.0,
    releaseTime = 1.0,
    amp = 1.0;

  var out, env;

  env = Linen.kr(gate, attackTime, 1.0, releaseTime, 2);
  out = env * amp * PlayBuf.ar(buf.numChannels, buf.bufnum, loop: 1);

}, [
  \buffer,
  \gate
]);
