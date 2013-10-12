Soundscape : Object {
  var <>elements,
    <>bufManager,
    <>outbus,
    <>masterChannel;

  init {

    arg params;

    this.elements = ();
    
    this.bufManager = params[\bufManager];

    if (params[\outbus] == nil, {
      params[\outbus] = 0;    
    });
    this.outbus = params[\outbus];

    1.0.wait();
    "Soundscape: initializing channels...".postln(); 
    this.init_channels();

    1.0.wait();
    "Soundscape: initializing elements...".postln();
    this.init_elements();
    
    1.0.wait();
    // initialize all elements
    this.elements.keysValuesDo({
      arg key, element;

      element.init((
        soundscape: this,
        bufManager: this.bufManager,
        key: key
      ));
    });
  }

  init_channels {
    this.masterChannel = MixerChannel.new(
      \masterChannel,
      Server.default,
      2,
      2,
      1.0,
      outbus: this.outbus
    );
    0.5.wait();
  }

  init_elements {
  
  }

  start_soundscape {
  
    "starting soundscape...".postln();
    {
      this.prepare_to_play();
      2.0.wait();
      this.play();
    }.fork();
  
  }

  prepare_to_play {

    this.elements.do({
      arg element;

      element.prepare_to_play();
    });
  
  }

  play {

    // play all elements
    this.elements.do({
      arg element;

      if (element != 0, {
        element.play();    
      });
    });
  
  }

}
