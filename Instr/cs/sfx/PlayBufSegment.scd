Instr("cs.sfx.PlayBuf", {
  
  arg buf,
    gate = 1,
    startTime = 0.0,
    attackTime = 1.0,
    sustainTime = 1.0,
    releaseTime = 1.0;

  var out,
    envShape,
    env;

  env = Env.adsr(
    attackTime: attackTime,
    decayTime: 0.0,

  )

  out = PlayBuf.ar(buf.numChannels, buf, trigger: gate);

}, [
  \buffer,
  \gate
]);
