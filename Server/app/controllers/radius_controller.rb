class RadiusController < ApplicationController

#
# Get draws within a radius from a coordinate
#
# * *Args*    :
#   - +id+ -> user's id
#   - +lat+ -> location's latitude
#   - +long+ -> location's longitude
#   - +long+ -> search's radius
# * *Returns* :
#   - Array [{"id" :  [draw's id] , "piggies" :  [number of piggies the draw is worth] , "latitude" : " [latitude] ", "longitude" : " [longitude] ", "challenge" :  [true or false] , "password" : " [draw's password] ", "description" : " [challenge's description] ", "creator_name" : " [creator's name] ", "creator_email" : " [creator's email] ", "times_guessed" : " [Times the draw was guessed before] ", "created" :  [date] , "check" :  [true or false]  }] 
#   - {"Error on trying to get the draws nearby by coordinates."}
#
	def getByCoordinates
	    @lat = params[:lat]
	    @long = params[:long]
	    @radius = params[:radius]
	    @user_id = params[:id]
	    if !@lat.nil? && !@long.nil? && !@radius.nil?
	    	@nearbys = Draw.near(@lat+","+@long, @radius, {:order => "distance", :units => :km})
	    	@final = Array.new
	    	@nearbys.each do |draw|
			  	creator = User.find(draw.id_creator)
			  	word = Word.find(draw.word_id)
			  	checkDraw = check(draw.id, @user_id)
			  	piggies = word.difficulty*2
			  	if draw.challenge
			  		piggies = piggies+1
			  	end
			  	elem = Hash[:id => "#{draw.id}", :piggies => "#{piggies}",:latitude => "#{draw.latitude}",:longitude => "#{draw.longitude}", :challenge => "#{draw.challenge}", :password => "#{draw.password}", :description => "#{draw.description}", :creator_name => "#{creator.name}", :creator_email => "#{creator.email}", :times_guessed => "#{DrawUser.where("id_draw = ?", draw.id).length}", :created => "#{draw.created_at}", :check => "#{checkDraw}"]
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


#
# Get draws within a radius from the name of a place
#
# * *Args*    :
#   - +id+ -> user's id
#   - +name+ -> location's name
#   - +long+ -> search's radius
# * *Returns* :
#   - Array [{"id" :  [draw's id] , "piggies" :  [number of piggies the draw is worth] , "latitude" : " [latitude] ", "longitude" : " [longitude] ", "challenge" :  [true or false] , "password" : " [draw's password] ", "description" : " [challenge's description] ", "creator_name" : " [creator's name] ", "creator_email" : " [creator's email] ", "times_guessed" : " [Times the draw was guessed before] ", "created" :  [date] , "check" :  [true or false]  }] 
#   - {"Error on trying to get the draws nearby by coordinates."}
#
	def getByName
		@radius = params[:radius]
	    @name = params[:name]
	    @user_id = params[:id]
	    if !@name.nil? && !@radius.nil?
	    	@nearbys = Draw.near(@name, @radius, {:order => "distance", :units => :km})
	    	logger.info(@nearbys.count)
	    	@final = Array.new
	    	@nearbys.each do |draw|
    			creator = User.find(draw.id_creator)
    			word = Word.find(draw.word_id)
    			checkDraw = check(draw.id, @user_id)
    			piggies = word.difficulty*2
			  	if draw.challenge
			  		piggies = piggies+1
			  	end
			  	elem = Hash[:id => "#{draw.id}", :piggies => "#{piggies}",:latitude => "#{draw.latitude}",:longitude => "#{draw.longitude}", :challenge => "#{draw.challenge}", :password => "#{draw.password}", :description => "#{draw.description}", :creator_name => "#{creator.name}", :creator_email => "#{creator.email}", :times_guessed => "#{DrawUser.where("id_draw = ?", draw.id).length}", :created => "#{draw.created_at}", :check => "#{checkDraw}"]
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


#
# Check if the user has done the draw before
#
# * *Args*    :
#   - +id+ -> user's id
#   - +draw_id+ -> draw's id
# * *Returns* :
#   - true
#   - false
#
	def check(draw_id,id)
		@draw_user = DrawUser.where("id_draw = ? AND id_user= ?", draw_id, id)
		if(@draw_user.empty?)
			return true
		else
			return false
		end
	end
end
