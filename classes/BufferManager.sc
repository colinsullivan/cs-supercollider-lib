/**
 *  @file       BufferManager.sc
 *
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

/**
 *  @class  Loads a group of buffers and stores them keyed by an identifier.
 **/
BufferManager : Object {
  
  var <>bufs,
    <>doneLoadingCallback,
    <>rootDir;

  /**
   *  Initialize
   *
   *  @param  {String}    params.rootDir - The root directory we will look
   *          for samples in.
   *  @param  {Function}  params.doneLoadingCallback - Callback to fire when
   *          samples are done loading.
   */
  init {
    arg params;

    this.rootDir = params[\rootDir];
    this.doneLoadingCallback = params[\doneLoadingCallback];
    
    this.bufs = ();

  }

  load_bufs {
    arg bufList,
      aCallback;

    var me = this;

    // if a callback was passed in, use that as the done loading callback
    if (aCallback != nil, {
      this.doneLoadingCallback = aCallback;    
    });

    // first we need to know about all the buffers we are trying to load
    bufList.do({
      arg bufData;

      var bufFileName = bufData[0],
        bufKey = bufData[1];

      this.bufs[bufKey] = 0;

    });

    // now actually load them all
    bufList.do({
      arg bufData;

      var bufFileName = bufData[0],
        bufKey = bufData[1];
      
      Buffer.read(
        Server.default,
        this.rootDir +/+ bufFileName,
        action: {
          arg buf;

          me.buf_loaded(bufKey, buf);
        };
      );
    });
  }

  buf_loaded {
    arg bufKey, buf, msg;

    this.bufs[bufKey] = buf;

    /*("loaded buf: " ++ bufKey).postln();*/

    // if all bufs are not zero
    if (this.bufs.any({ arg item; item == 0; }) == false, {
      // finish loading
      this.bufs_all_loaded();
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

  bufs_all_loaded {

    this.doneLoadingCallback.value();

  }
}
