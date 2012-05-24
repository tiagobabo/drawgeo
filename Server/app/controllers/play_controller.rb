class PlayController < ApplicationController
	def guess
		@player = User.find(params[:id])
		@draw = Draw.find(params[:draw_id])
		@guess = params[:guess]
		@word = Word.find(@draw.word_id)
		@author = User.find(@draw.id_creator)
		if(@guess == @word.word && @player.id != @author.id)
			@player.update_attribute(:num_done, @player.num_done + 1)
			@author.update_attribute(:num_success, @author.num_success + 1)
			@player.update_attribute(:piggies, @player.piggies + (@word.difficulty * 2))
			@author.update_attribute(:piggies, @author.piggies + (@word.difficulty))

			if(@draw.challenge)
				@player.update_attribute(:piggies, @player.piggies + 1)
				@author.update_attribute(:piggies, @author.piggies + 1)
			end
			respond_to do |format|
		      format.json { render :json => {:status => "Ok", :word => @word}.to_json }
		    end
		else
			respond_to do |format|
		      format.json { render :json => {:status => "No"}.to_json }
		    end
		end
	end
end
