Instr("cs.percussion.Noisy.NoisyMono", {

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
  inverseVelocity = (1.0 - velocity);

  /*outEnvShape = Env.adsr(
    attackTime: 0.1,
    decayTime: 0.3,
    sustainLevel: 1.0,
    releaseTime: 1.0,
    peakLevel: 1.0
  );
  outEnvShape.releaseNode = 2;*/
  outEnvShape = Env.perc(
    attackTime: inverseVelocity.exprange(0.001, 1.1),
    releaseTime: velocity.range(0.2, 1.25)
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
    levelScale: velocity.exprange(7000, 20000),
    timeScale: velocity.range(0.01, 1.0)
  );
  //out = Impulse.ar(0);
  out = Dust.ar(dustDensityEnv);

  //out = BPF.ar(out, freq, 0.01);
  resonanceMin = 0.05;
  resonanceMax = 0.1;
  out = RLPF.ar(
    out,
    freq * 4.0,
    inverseVelocity.exprange(resonanceMin, resonanceMax)
  );

  //out = MantissaMask.ar(out, 23);

  //out = MembraneCircle.ar(out, MouseX.kr(0.01, 0.1), MouseY.kr(0.999999, 0.999, 1));

  out = amp * (outEnv * out);
}, [
  \freq,
  \amp,
  \gate
]);


// just double for different randomized noise effect in each channel
Instr("cs.percussion.Noisy.NoisyStereo", {
  arg freq = 440,
    amp = 0.9,
    gate = 0;

  var out;

  out = [
    Instr.ar("cs.percussion.Noisy.NoisyMono", (
      freq: freq,
      amp: amp,
      gate: gate
    )),
    Instr.ar("cs.percussion.Noisy.NoisyMono", (
      freq: freq,
      amp: amp,
      gate: gate
    ))
  ];

}, [
  \freq,
  \amp,
  \gate
]);
