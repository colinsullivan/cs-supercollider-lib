/**
 *  @class  PerformanceEnvironmentComponent   A component of a performance
 *  environment that typically has a GUI window.
 **/
PerformanceEnvironmentComponent : Object {
  var <>origin,
    <>window,
    <>uc33Controller,
    <>softStepController,
    <>init_done_callback;

  *new {
    arg params;

    ^super.new.init(params);
  }

  init {
    arg params;

    ("  * Starting " + this.gui_window_title()).postln;
    ("----------------------------------------").postln;

    this.origin = params['origin'];
    this.init_done_callback = params['init_done_callback'];
  
  }

  init_gui {
    arg params;
   
    this.window = params['window'];
    this.window.bounds = this.window.bounds.moveToPoint(this.origin);
    this.window.name = this.gui_window_title();
  }

  init_external_controller_mappings {
    var uc33Port,
      softStepPort;
    
    uc33Port = MIDIIn.findPort("UC-33 USB MIDI Controller", "Port 1");
    /*uc33Port = MIDIIn.findPort("(out) SuperCollider", "(out) SuperCollider");*/
    softStepPort = MIDIIn.findPort("SoftStep Share", "SoftStep Share");

    if (uc33Port != nil, {
      // sub-classes should use this UC33Ktl instance to assign knobs and such.
      this.uc33Controller = UC33Ktl.new(
        uc33Port.uid
      );
    }, {
      // sub-classes should check to see if uc33Controller is nil to determine
      // if it is currently connected.
      this.uc33Controller = nil;
    });

    if (softStepPort != nil, {
      this.softStepController = SoftStepKtl.new(softStepPort.uid);    
    }, {
      this.softStepController = nil;
    });
    
  }

  /**
   *  Called to set title of GUI window.  Default is the name of the class
   *  of the component.
   **/
  gui_window_title {
    ^this.class.asString();
  }
}
