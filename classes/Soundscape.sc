Soundscape : Object {
  var <>elements,
    <>bufs,
    <>masterChannel;

  init {

    arg args;

    var bufsToLoad = args['bufsToLoad'],
      projSfxPath = args['projSfxPath'];

    this.bufs = ();

    this.elements = ();
  
    this.masterChannel = MixerChannel.new(
      \masterChannel,
      Server.default,
      2,
      2,
      1.0,
      //outbus: 0
    );

    "Soundscape: initializing buffers...".postln();
    this.init_bufs();
   
    "Soundscape: initializing channels...".postln(); 
    this.init_channels();

    "Soundscape: initializing elements...".postln();
    this.init_elements();

    // initialize all elements
    this.elements.keysValuesDo({
      arg key, element;

      element.init((
        soundscape: this,
        key: key
      ));
    });

    // if there are buffers to load
    if (bufsToLoad.size() > 0, {
      "starting ot load buffers".postln();
    }, {
      "no buffers to load...".postln();

      this.start_soundscape();
    });

    // initialize all audio buffers and start soundscape
    bufsToLoad.do({
      arg bufData;

      var bufFileName = bufData[0],
        bufKey = bufData[1];

      this.load_buf(projSfxPath +/+ bufFileName, bufKey);

    });
  }

  init_channels {
  
  }

  init_elements {
  
  }

  init_bufs {
  
  }
  
  load_buf {
    arg bufPath, bufKey;
    var me = this;

    Buffer.read(
      Server.default,
      bufPath,
      action: {
        arg buf;

        me.buf_loaded(bufKey, buf);
      };
    );
  
  }
  
  buf_loaded {
    arg bufKey, buf, msg;

    this.bufs[bufKey] = buf;

    /*("loaded buf: " ++ bufKey).postln();*/

    // if all bufs are not zero
    if (this.bufs.any({ arg item; item == 0; }) == false, {
      // finish loading
      this.start_soundscape();
    }, {
      msg = "bufs to load:";

      this.bufs.keysValuesDo({
        arg bufKey, bufValue;

        if (bufValue == 0, {
          msg = msg ++ bufKey ++ ", ";
        });
      });

      /*msg.postln();*/
    });
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
