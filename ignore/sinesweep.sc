(

  var m,
    mBounds,
    projRoot = "/Users/colin/Documents/Stanford/Courses/250b/project",
    projSfx = projRoot +/+ "raw-samples";

  s.quit;
  
  s.options.outDevice = "JackRouter";
  s.options.blockSize = 8;
  s.boot();
  m = s.meter();


  mBounds = m.window.bounds;
  /*mBounds.left = 1680;
  mBounds.top = 1000;*/
  mBounds.left = 1440;
  mBounds.top = 900;
  
  m.window.setTopLeftBounds(mBounds);

  Instr.dir = projRoot +/+ "lib/";
  Instr.loadAll();

  s.doWhenBooted({
    var outPatch;

    2.0.wait();
    
    outPatch = Patch({
      var freqEnv = XLine.kr(10, 22000, 30.0, doneAction: 2),
        out;
      freqEnv.poll();
      out = SinOsc.ar(freqEnv);

      [out, out];
    });

    "preparing".postln();

    outPatch.prepareForPlay();

    1.0.wait();

    "playing".postln();

    outPatch.play();

  });

)
