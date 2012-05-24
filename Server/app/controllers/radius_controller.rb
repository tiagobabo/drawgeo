class RadiusController < ApplicationController
	def get
		#http://localhost:3000/radius/get?lat=_&long=_&radius=_
		#Example: http://localhost:3000/radius/get?lat=41.148414&long=-8.611221&radius=300&format=json
	    @lat = params[:lat]
	    @long = params[:long]
	    @radius = params[:radius]

	    if !@lat.nil? && !@long.nil? && !@radius.nil?
	    	@nearbys = Draw.near(@lat+","+@long, @radius, {:order => "distance", :units => :km})

		    respond_to do |format|
		    	format.html
		      	format.json { render :json => @nearbys }
		    end
		else
			respond_to do |format|
				format.html
		      	format.json { render :json => "Error" }
		    end
		end
	end
end
