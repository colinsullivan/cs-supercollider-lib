Instr("cs.percussion.Impulsive", {

  arg freq = 440,
    amp = 0.9,
    gate = 0;

  var out,
    dustDensityEnv,
    dustDensityEnvShape,
    outEnvShape,
    outEnv,
    resonanceMin,
    resonanceMax,
    velocity,
    inverseVelocity;

  velocity = Latch.kr(gate, gate);
  inverseVelocity = 1.0 - velocity;

  /*outEnvShape = Env.adsr(
    attackTime: 0.1,
    decayTime: 0.3,
    sustainLevel: 1.0,
    releaseTime: 1.0,
    peakLevel: 1.0
  );
  outEnvShape.releaseNode = 2;*/
  outEnvShape = Env.perc(
    attackTime: 0,
    releaseTime: 1.25
  );
  
  outEnv = EnvGen.ar(
    outEnvShape,
    gate: gate,
    doneAction: 2
  );

  dustDensityEnvShape = Env.perc(
    attackTime: 0.01,
    releaseTime: 1.0,
    curve: -6
  );
  dustDensityEnv = EnvGen.ar(
    dustDensityEnvShape,
    gate,
    0,
    levelScale: velocity.exprange(1000, 20000),
    timeScale: velocity.exprange(0.1, 1.0)
  );
  //out = Impulse.ar(0);
  out = Dust.ar(dustDensityEnv);

  //out = BPF.ar(out, freq, 0.01);
  resonanceMin = 0.05;
  resonanceMax = 0.2;
  out = RLPF.ar(
    out,
    freq * 4.0,
    inverseVelocity.exprange(resonanceMin, resonanceMax)
  );

  out = [out, DelayC.ar(out, delaytime: 0.005)];

  //out = MantissaMask.ar(out, 23);

  //out = MembraneCircle.ar(out, MouseX.kr(0.01, 0.1), MouseY.kr(0.999999, 0.999, 1));

  out = amp * (outEnv * out);
}, [
  \freq,
  \amp,
  \gate
]);
