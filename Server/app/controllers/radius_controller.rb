class RadiusController < ApplicationController
	def getByCoordinates
		#http://localhost:3000/radius/getByCoordinates?lat=_&long=_&radius=_
		#Example: http://localhost:3000/radius/getByCoordinates?lat=41.148414&long=-8.611221&radius=300&format=json
	    @lat = params[:lat]
	    @long = params[:long]
	    @radius = params[:radius]

	    if !@lat.nil? && !@long.nil? && !@radius.nil?
	    	@nearbys = Draw.near(@lat+","+@long, @radius, {:order => "distance", :units => :km})
	    	@final = Array.new
	    	@nearbys.each do |draw|
			  	creator = User.find(draw.id_creator)
			  	word = Word.find(draw.word_id)
			  	elem = Hash[:id => "#{draw.id}", :piggies => "#{word.difficulty * 2}",:latitude => "#{draw.latitude}",:longitude => "#{draw.longitude}", :challenge => "#{draw.challenge}", :password => "#{draw.password}", :description => "#{draw.description}", :creator_name => "#{creator.name}", :creator_email => "#{creator.email}", :times_guessed => "#{DrawUser.where("id_draw = ?", draw.id).length}", :created => "#{draw.created_at}"]
			  	@final << elem
			end
		    respond_to do |format|
		    	format.html
		      	format.json { render :json => @final.to_json }
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
	    	@final = Array.new
	    	@nearbys.each do |draw|
    			creator = User.find(draw.id_creator)
    			word = Word.find(draw.word_id)
			  	elem = Hash[:id => "#{draw.id}", :piggies => "#{word.difficulty * 2}",:latitude => "#{draw.latitude}",:longitude => "#{draw.longitude}", :challenge => "#{draw.challenge}", :password => "#{draw.password}", :description => "#{draw.description}", :creator_name => "#{creator.name}", :creator_email => "#{creator.email}", :times_guessed => "#{DrawUser.where("id_draw = ?", draw.id).length}", :created => "#{draw.created_at}"]
			  	@final << elem
			end
		    respond_to do |format|
		    	format.html
		      	format.json { render :json => @final.to_json }
		    end
		else
			respond_to do |format|
				format.html
		      	format.json { render :json => "Error on trying to get the draws nearby by name." }
		    end
		end
	end
end
