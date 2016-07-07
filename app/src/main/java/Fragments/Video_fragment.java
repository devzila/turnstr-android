package Fragments;


import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.example.R;

import adapter.FragmentInterface;

public class Video_fragment extends Fragment implements FragmentInterface,SurfaceHolder.Callback {
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	private Camera myCamera;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
		surfaceView = (SurfaceView) rootView.findViewById(R.id.camera_preview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		return rootView;
	}

	@Override
	public void fragmentBecameVisible() {
		System.out.println("Video");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (surfaceHolder.getSurface() == null){
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			myCamera.stopPreview();
		} catch (Exception e){
			// ignore: tried to stop a non-existent preview
		}

		// make any resize, rotate or reformatting changes here

		// start preview with new settings

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}
	private Camera getCameraInstance(){
		// TODO Auto-generated method stub
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		}
		catch (Exception e){
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}
}
