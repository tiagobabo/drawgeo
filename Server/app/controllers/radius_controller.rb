class RadiusController < ApplicationController
	def getByCoordinates
		#http://localhost:3000/radius/getByCoordinates?lat=_&long=_&radius=_
		#Example: http://localhost:3000/radius/getByCoordinates?lat=41.148414&long=-8.611221&radius=300&format=json
	    @lat = params[:lat]
	    @long = params[:long]
	    @radius = params[:radius]

	    if !@lat.nil? && !@long.nil? && !@radius.nil?
	    	@nearbys = Draw.near(@lat+","+@long, @radius, {:order => "distance", :units => :km})
		    respond_to do |format|
		    	format.html
		      	format.json { render :json => @nearbys.to_json(:only => [ :id, :latitude, :longitude, :challenge, :description ]) }
		    end
		else
			respond_to do |format|
				format.html
		      	format.json { render :json => "Error on trying to get the draws nearby by coordinates." }
		    end
		end
	end

	def getByName
		#http://localhost:3000/radius/getByName?name=_
		#Example: http://localhost:3000/radius/getByName?name="Tokyo"format=json
		@radius = params[:radius]
	    @name = params[:name]

	    if !@name.nil? && !@radius.nil?
	    	@nearbys = Draw.near(@name, @radius, {:order => "distance", :units => :km})
	    	logger.info(@nearbys.count)

		    respond_to do |format|
		    	format.html
		      	format.json { render :json => @nearbys.to_json(:only => [ :id, :latitude, :longitude, :challenge, :description ]) }
		    end
		else
			respond_to do |format|
				format.html
		      	format.json { render :json => "Error on trying to get the draws nearby by name." }
		    end
		end
	end
end
