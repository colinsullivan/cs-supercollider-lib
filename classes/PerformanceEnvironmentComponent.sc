/**
 *  @class  PerformanceEnvironmentComponent   A component of a performance
 *  environment that has a GUI window.
 **/
PerformanceEnvironmentComponent : Object {
  var <>origin,
    <>interface,
    <>window,
    <>init_done_callback;

  *new {
    arg params;

    ^super.new.init(params);
  }

  init {
    arg params;
    var me = this;

    ("  * Starting " + this.gui_window_title()).postln;
    ("----------------------------------------").postln;

    this.origin = params['origin'];
    this.init_done_callback = params['init_done_callback'];
 
    // initialize loading of samples
    this.load_samples({

      // when samples are finished, load interface
      me.interface = Interface({
        me.load_environment();
      }).onPlay_({
        me.on_play();
      }).onStop_({
        me.on_stop();
      });

      me.interface.gui = {
        arg layout, metaPatch;
        me.init_gui((
          window: layout.parent.parent,
          layout: layout,
          metaPatch: metaPatch
        ));
        me.init_external_controller_mappings();
      };

      {
        me.interface.gui();
        me.init_done_callback.value();
      }.defer(1);
    });
  }
  
  /**
   *  Load all audio buffers required for this component.
   *  
   *  @param  Function  callback  To call when done loading samples.
   **/
  load_samples {
    arg callback;
    // subclasses should load samples before using callback
    callback.value();
  }

  load_environment {
  
  }


  init_gui {
    arg params;
   
    this.window = params['window'];

    this.window.bounds = this.window.bounds.moveToPoint(this.origin);
    this.window.name = this.gui_window_title();
  }
 
  /**
   *  Called when the play button is pressed on the interface.
   **/
  on_play {
  }

  /**
   *  Called when the stop button is pressed on the interface.
   **/
  on_stop {
  
  }

  init_external_controller_mappings {
    
  }

  /**
   *  Called to set title of GUI window.  Default is the name of the class
   *  of the component.
   **/
  gui_window_title {
    ^this.class.asString();
  }
}
