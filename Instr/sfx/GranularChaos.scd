Instr("sfx.GranularChaos", {
  arg buffer,
    envbuf = -1,
    gate = 0,
    amp = 0.75, // this instrument is loud
    pitch = 1.0,
    pointer = 0.0,
    // called when release envelope is done and synth will be freed
    done_callback = {};
    
    var out, filelength, env, dir, envShape, theGate;

    /*pitch = MouseY.kr(-2.0, 2.0);*/
    /*// pointer - move from beginning to end of soundfile over 15 seconds*/
    /*pointer = MouseX.kr(0.0, 1.0);*/

    envShape = Env.adsr(0.1, 0.3, 1.0, 2.0, 1.0);
    envShape.releaseNode = 2;
    env = EnvGen.kr(
      envShape,
      gate,
      doneAction: 2
    );
    /*Done.kr(env).onTrig(done_callback);*/
    out = Warp1.ar(
      numChannels: buffer.numChannels,
      bufnum: buffer,
      pointer: pointer,
      freqScale: pitch,
      windowSize: 0.1,
      envbufnum: envbuf,
      overlaps: 8,
      windowRandRatio: 0.1,
      interp: 4
    );

    out = amp * (env * out);

    /*out = TGrains3.ar(2, Dust.ar(0.5), buffer, pitch, pointer, window: envbuf);*/
}, [
  \buffer.asSpec(),
  \buffer.asSpec(),
  \gate.asSpec(),
  \amp.asSpec(),
  ControlSpec(-1.5, 1.5),
  ControlSpec(0.0, 1.0)
]);

