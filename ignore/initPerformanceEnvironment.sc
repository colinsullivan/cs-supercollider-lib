(
  var m, mBounds;
  /*s.options.inDevice = "PreSonus FIREPOD (2112)";*/
  /*s.options.inDevice = "SF + 1818";*/
  /*s.options.inDevice = "AudioBox 1818 VSL ";*/
  /*s.options.inDevice = "Soundflower (64ch)";*/

  s.options.outDevice = "Soundflower (64ch)";
  s.quit;
  s.options.sampleRate = 48000;
  /*s.options.hardwareBufferSize = 256;*/
  s.boot();
  /*s.latency = 0;*/
  m = s.meter();

  // move level meter to bottom right of screen
  mBounds = m.window.bounds;
  /*mBounds.left = 1680;
  mBounds.top = 1000;*/
  mBounds.left = 1440;
  mBounds.top = 900;
  
  m.window.setTopLeftBounds(mBounds);
  
  GUI.qt;
  Instr.dir = "/Users/colin/Projects/cs-supercollider-lib/Instr/";
  Instr.loadAll();

  s.doWhenBooted({
    MIDIClient.init();
    MIDIPort.init();
    MIDIIn.connectAll();
    
    "----------------------------------------".postln;
    "* Starting Performance Environment...".postln;
    "----------------------------------------".postln;
    
    ~performanceEnvironment = PerformanceEnvironment.new();
  });

)
